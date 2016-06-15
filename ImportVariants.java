package nhs.genetics.cardiff;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeaderLine;
import nhs.genetics.cardiff.framework.GenomeVariant;
import org.neo4j.driver.v1.*;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Functions for importing VCF variants into Neo4j and ensuring schema
 *
 * @author  Matt Lyon
 * @version 1.0
 * @since   2016-05-09
 */
public class ImportVariants {
    private static final Logger log = Logger.getLogger(ImportVariants.class.getName());

    private VCFFileReader vcfFileReader;
    private Driver driver;
    private HashMap<String, HashMap <String, Object>> metaData = new HashMap<String, HashMap<String, Object>>();
    private HashSet<GenomeVariant> blackListVariants = new HashSet<>();

    public ImportVariants(File vcfFile, Driver driver){
        this.vcfFileReader = new VCFFileReader(vcfFile);
        this.driver = driver;
    }

    /** @throws NoSuchFieldException Metadata or genotypes not provided */
    void parseVCFMetaData() throws NoSuchFieldException {
        log.log(Level.INFO, "Parsing VCF metadata ...");

        if (!vcfFileReader.getFileHeader().hasGenotypingData()){
            throw new NoSuchFieldException("VCF does not contain genotype data");
        }

        for (VCFHeaderLine line : vcfFileReader.getFileHeader().getMetaDataInInputOrder()) {
            if (line.getKey().equals("SAMPLE")) {
                HashMap<String, Object> keyValuePairs = new HashMap<>();

                //split out key value pairs
                for (String keyValuePair : line.getValue().split(",")) {
                    String[] keyValue = keyValuePair.split("=");
                    keyValuePairs.put(keyValue[0].replace("<", ""), keyValue[1].replace(">", ""));
                }

                //cast variables
                keyValuePairs.put("PipelineVersion", Integer.parseInt((String) keyValuePairs.get("PipelineVersion")));

                //store associated with sample
                metaData.put((String) keyValuePairs.get("ID"), keyValuePairs);

            }
        }

        if (metaData.size() != vcfFileReader.getFileHeader().getNGenotypeSamples()){
            throw new NoSuchFieldException("Meta data missing for one or more samples");
        }

    }

    void addSamplesAndAnalyses() {
        log.log(Level.INFO, "Adding samples and analyses ...");

        for (Map.Entry<String, HashMap<String, Object>> sample : metaData.entrySet()) {

            //create sample, runInfo and link
            try (Session session = driver.session()) {
                session.run("MERGE (s:Sample {sampleId:{ID}, tissue:{Tissue}}) CREATE UNIQUE (s)-[:HAS_DATA]->(:Dataset {worklistId:{WorklistId}, seqId:{SeqId}, assay:{Assay}, pipelineName:{PipelineName}, pipelineVersion:{PipelineVersion}, remoteBamFilePath:{RemoteBamFilePath}, remoteVcfFilePath:{RemoteVcfFilePath}});",
                        sample.getValue()
                );
            }

        }

    }

    void setBlackList(File blackListVcfFile){
        log.log(Level.INFO, "Parsing blacklist ...");

        int n = 0;
        GenomeVariant genomeVariant;
        VCFFileReader blackListVcfFileReader = new VCFFileReader(blackListVcfFile);
        Iterator<VariantContext> blackListVariantContextIterator = blackListVcfFileReader.iterator();

        //read variant VCF file
        while (blackListVariantContextIterator.hasNext()) {
            VariantContext blackListVariantContext = blackListVariantContextIterator.next();

            //loop over alternative alleles
            for (Allele allele : blackListVariantContext.getAlternateAlleles()){

                genomeVariant = new GenomeVariant(blackListVariantContext.getContig(), blackListVariantContext.getStart(), blackListVariantContext.getReference().getBaseString(), allele.getBaseString());
                genomeVariant.convertToMinimalRepresentation();

                blackListVariants.add(genomeVariant);

            }

        }

        log.log(Level.INFO, "Added " + n + " variants to exclusion list");
    }

    void addVariants() {
        log.log(Level.INFO, "Adding variants ...");

        GenomeVariant genomeVariant;
        Iterator<VariantContext> variantContextIterator = vcfFileReader.iterator();

        //read variant VCF file
        while (variantContextIterator.hasNext()) {
            VariantContext variantContext = variantContextIterator.next();

            //skip filtered and non-variant loci
            if (!variantContext.isFiltered() && variantContext.isVariant()){
                Iterator<Genotype> genotypeIterator = variantContext.getGenotypes().iterator();

                //read genotypes
                while (genotypeIterator.hasNext()) {
                    Genotype genotype = genotypeIterator.next();

                    //skip no-calls, hom-refs,  mixed genotypes or alleles covered by nearby indels
                    if (genotype.isNoCall() || genotype.isHomRef() || genotype.isFiltered()){
                        continue;
                    }
                    if (genotype.isMixed()){
                        log.log(Level.WARNING, genotype.getSampleName() + ": " + variantContext.getContig() + " " + variantContext.getStart() + " " + variantContext.getReference() + variantContext.getAlternateAlleles().toString() + " has mixed genotype ( " + genotype.getGenotypeString() + " ) and could not be added.");
                        continue;
                    }
                    if (genotype.getPloidy() != 2 || genotype.getAlleles().size() != 2) {
                        log.log(Level.WARNING, "Allele " + genotype.getAlleles().toString() + " is not diploid and could not be added.");
                        continue;
                    }
                    if (genotype.getAlleles().get(0).getBaseString().equals("*") || genotype.getAlleles().get(1).getBaseString().equals("*")) {
                        continue;
                    }

                    //add new variants to the DB
                    if (genotype.isHom()){

                        genomeVariant = new GenomeVariant(variantContext.getContig(), variantContext.getStart(), variantContext.getReference().getBaseString(), genotype.getAlleles().get(1).getBaseString());
                        genomeVariant.convertToMinimalRepresentation();

                        if (blackListVariants.contains(genomeVariant)) {
                            log.log(Level.WARNING, "Excluded " + genomeVariant.toString());
                            continue;
                        }

                        try (Session session = driver.session()) {
                            session.run("MATCH (s:Sample {sampleId:{sampleId}})-[:HAS_DATA]->(a:Dataset {seqId:{seqId}}) " +
                                    "MERGE (v:Variant {variantId:{variantId}}) ON CREATE SET v :Annotate " +
                                    "CREATE UNIQUE (a)-[rel:HAS_HOM_VARIANT {gtQuality:{GQ}}]->(v)", Values.parameters(
                                    "sampleId", genotype.getSampleName(),
                                    "seqId", metaData.get(genotype.getSampleName()).get("SeqId"),
                                    "variantId", genomeVariant.toString(),
                                    "GQ", genotype.getGQ())
                            );
                        }

                    } else if (genotype.isHet()){

                        genomeVariant = new GenomeVariant(variantContext.getContig(), variantContext.getStart(), variantContext.getReference().getBaseString(), genotype.getAlleles().get(1).getBaseString());
                        genomeVariant.convertToMinimalRepresentation();

                        if (blackListVariants.contains(genomeVariant)) {
                            log.log(Level.WARNING, "Excluded " + genomeVariant.toString());
                            continue;
                        }

                        try (Session session = driver.session()) {
                            session.run("MATCH (s:Sample {sampleId:{sampleId}})-[:HAS_DATA]->(a:Dataset {seqId:{seqId}}) " +
                                    "MERGE (v:Variant {variantId:{variantId}}) ON CREATE SET v :Annotate " +
                                    "CREATE UNIQUE (a)-[:HAS_HET_VARIANT {gtQuality:{GQ}}]->(v)", Values.parameters(
                                    "sampleId", genotype.getSampleName(),
                                    "seqId", metaData.get(genotype.getSampleName()).get("SeqId"),
                                    "variantId", genomeVariant.toString(),
                                    "GQ", genotype.getGQ()
                            )
                            );
                        }

                        if (genotype.isHetNonRef()){

                            //TODO check alleles
                            genomeVariant = new GenomeVariant(variantContext.getContig(), variantContext.getStart(), variantContext.getReference().getBaseString(), genotype.getAlleles().get(0).getBaseString());
                            genomeVariant.convertToMinimalRepresentation();

                            if (blackListVariants.contains(genomeVariant)) {
                                log.log(Level.WARNING, "Excluded " + genomeVariant.toString());
                                continue;
                            }

                            try (Session session = driver.session()) {
                                session.run("MATCH (s:Sample {sampleId:{sampleId}})-[:HAS_DATA]->(a:Dataset {seqId:{seqId}}) " +
                                        "MERGE (v:Variant {variantId:{variantId}}) ON CREATE SET v :Annotate " +
                                        "CREATE UNIQUE (a)-[:HAS_HET_VARIANT {gtQuality:{GQ}}]->(v)", Values.parameters(
                                        "sampleId", genotype.getSampleName(),
                                        "seqId", metaData.get(genotype.getSampleName()).get("SeqId"),
                                        "variantId", genomeVariant.toString(),
                                        "GQ", genotype.getGQ())
                                );
                            }

                        }

                    } else {
                        log.log(Level.WARNING, "Inheritance unknown: " + variantContext.toString() + " and could not be added.");
                    }

                }

            }

        }

    }

}
