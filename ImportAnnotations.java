package nhs.genetics.cardiff;

import htsjdk.tribble.readers.LineIteratorImpl;
import htsjdk.tribble.readers.LineReaderUtil;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFCodec;
import nhs.genetics.cardiff.framework.GenomeVariant;
import org.neo4j.driver.v1.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Functions for importing annotations into Neo4j and ensuring schema
 *
 * @author  Matt Lyon
 * @version 1.0
 * @since   2016-05-09
 */
public class ImportAnnotations {
    private static final Logger log = Logger.getLogger(ImportAnnotations.class.getName());

    private Driver driver;
    private ArrayList<VEPRecord> vepRecords;

    public ImportAnnotations(ArrayList<VEPRecord> vepRecords, Driver driver){
        this.driver = driver;
        this.vepRecords = vepRecords;
    }

    static ArrayList<GenomeVariant> getAnnotationQueue(Driver driver){
        log.log(Level.INFO, "Exporting variants for annotation ...");

        ArrayList<GenomeVariant> genomeVariants = new ArrayList<>();

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (v:Annotate) REMOVE v:Annotate return v.variantId;");

            while (result.hasNext()) {
                GenomeVariant genomeVariant = new GenomeVariant(result.next().get("v.variantId").asString());
                genomeVariants.add(genomeVariant);
            }

        }

        log.log(Level.INFO, "Found " + genomeVariants.size() + " variants for annotation");

        return genomeVariants;
    }

    void importVEPRecords() throws IOException {
        log.log(Level.INFO, "Importing VEP annotations ...");

        for (VEPRecord vepRecord : vepRecords) {
            GenomeVariant genomeVariant = convertVepInputToGenomeVariant(vepRecord.getInput());

            try (Session session = driver.session()) {
                session.run("MATCH (v:Variant {variantId:{variantId}}) SET v.mostSevereConsequence = {mostSevereConsequence};",
                        Values.parameters(
                                "variantId", genomeVariant.toString(),
                                "mostSevereConsequence",vepRecord.getMostSevereConsequence())
                );
            }

            importColocatedVariants(vepRecord, genomeVariant);
            importTranscriptConsequences(vepRecord, genomeVariant);

        }
    }

    private void importColocatedVariants(VEPRecord vepRecord, GenomeVariant genomeVariant){

        if (vepRecord.getColocatedVariants() != null){

            vepRecord.getColocatedVariants().splitIdField();
            vepRecord.getColocatedVariants().setMafs(); /* Overcome JSON field error */

            try (Session session = driver.session()) {
                session.run("MATCH (v:Variant {variantId:{variantId}}) " +
                        "SET v.dbSnpId = {dbSnpId}, v.exacAfrMaf = {exacAfrMaf}, v.exacAmrMaf = {exacAmrMaf}, v.exacEasMaf = {exacEasMaf}, v.exacFinMaf = {exacFinMaf}, v.exacNfeMaf = {exacNfeMaf}, v.exacOthMaf = {exacOthMaf}, v.exacSasMaf = {exacSasMaf}, v.afr1kgMaf = {afr1kgMaf}, v.amr1kgMaf = {amr1kgMaf}, v.eas1kgMaf = {eas1kgMaf}, v.eur1kgMaf = {eur1kgMaf}, v.sas1kgMaf = {sas1kgMaf}, v.aaEspMaf = {aaEspMaf}, v.eaEspMaf = {eaEspMaf};",
                        Values.parameters(
                                "variantId", genomeVariant.toString(),
                                "dbSnpId", vepRecord.getColocatedVariants().getDbSnpId(),
                                "exacAfrMaf", vepRecord.getColocatedVariants().getExacAfrMaf(),
                                "exacAmrMaf", vepRecord.getColocatedVariants().getExacAmrMaf(),
                                "exacEasMaf", vepRecord.getColocatedVariants().getExacEasMaf(),
                                "exacFinMaf", vepRecord.getColocatedVariants().getExacFinMaf(),
                                "exacNfeMaf", vepRecord.getColocatedVariants().getExacNfeMaf(),
                                "exacOthMaf", vepRecord.getColocatedVariants().getExacOthMaf(),
                                "exacSasMaf", vepRecord.getColocatedVariants().getExacSasMaf(),
                                "afr1kgMaf", vepRecord.getColocatedVariants().getAfr1kgMaf(),
                                "amr1kgMaf", vepRecord.getColocatedVariants().getAmr1kgMaf(),
                                "eas1kgMaf", vepRecord.getColocatedVariants().getEas1kgMaf(),
                                "eur1kgMaf", vepRecord.getColocatedVariants().getEur1kgMaf(),
                                "sas1kgMaf", vepRecord.getColocatedVariants().getSas1kgMaf(),
                                "aaEspMaf", vepRecord.getColocatedVariants().getAaEspMaf(),
                                "eaEspMaf", vepRecord.getColocatedVariants().getEaEspMaf()
                        )
                );
            }

        }

    }

    private void importTranscriptConsequences(VEPRecord vepRecord, GenomeVariant genomeVariant) throws IOException {

        if (vepRecord.getTranscriptConsequences() != null){
            for (TranscriptConsequence transcriptConsequence : vepRecord.getTranscriptConsequences()){
                if (filterTranscriptConsequences(transcriptConsequence)) continue;

                //TODO mutation taster (RefSeq)

                //split domains
                HashMap<String, ArrayList<String>> domains = Domain.convertToMap(transcriptConsequence.getDomains());

                try (Session session = driver.session()) {
                    session.run(
                            "MATCH (v:Variant {variantId:{variantId}}) MERGE (f:Feature {featureId:{featureId}, featureType:{featureType}, strand:{strand}}) SET f.ccdsId = {ccdsId} MERGE (s:Symbol {symbolId:{symbolId}}) CREATE UNIQUE (v)-[:IN_SYMBOL]->(s)-[:HAS_FEATURE]->(f) CREATE UNIQUE (v)-[rel:HAS_ANNOTATION]->(f) SET rel.hgvsc = {hgvsc}, rel.hgvsp = {hgvsp}, rel.siftPrediction = {siftPrediction}, rel.polyphenPrediction = {polyphenPrediction}, rel.siftScore = {siftScore}, rel.polyphenScore = {polyphenScore}, rel.codons = {codons},rel.consequences = {consequences}, rel.prositeProfiles = {prositeProfiles}, rel.superfamilyDomains = {superfamilyDomains}, rel.smartDomains = {smartDomains}, rel.gene3D = {gene3D}, rel.hmmPanther = {hmmPanther},rel.pfamDomain = {pfamDomain}, rel.impact = {impact}, rel.location = {location};",
                            Values.parameters(
                                    "variantId", genomeVariant.toString(),
                                    "symbolId", transcriptConsequence.getGeneSymbol(),
                                    "featureId", transcriptConsequence.getTranscriptId(),
                                    "featureType", transcriptConsequence.getBiotype(),
                                    "strand", transcriptConsequence.getStrand(),
                                    "hgvsc",transcriptConsequence.getHgvsc(),
                                    "hgvsp",transcriptConsequence.getHgvsp(),
                                    "siftPrediction",transcriptConsequence.getSiftPrediction(),
                                    "polyphenPrediction",transcriptConsequence.getPolyphenPrediction(),
                                    "siftScore",transcriptConsequence.getSiftScore(),
                                    "polyphenScore",transcriptConsequence.getPolyphenScore(),
                                    "codons",transcriptConsequence.getCodons(),
                                    "consequences", transcriptConsequence.getConsequenceTerms(),
                                    "prositeProfiles",domains.get("PROSITE_profiles"),
                                    "superfamilyDomains",domains.get("Superfamily_domains"),
                                    "smartDomains",domains.get("SMART_domains"),
                                    "gene3D",domains.get("Gene3D"),
                                    "hmmPanther",domains.get("hmmpanther"),
                                    "pfamDomain",domains.get("Pfam_domain"),
                                    "ccdsId", transcriptConsequence.getCcds(),
                                    "impact", transcriptConsequence.getImpact(),
                                    "location", transcriptConsequence.getExonOrIntronNumber()
                            ));
                }

                if (transcriptConsequence.getCanonical() != null && transcriptConsequence.getCanonical()){
                    try (Session session = driver.session()) {
                        session.run("MATCH (f:Feature {featureId:{featureId}}) SET f :Canonical;",
                                Values.parameters("featureId", transcriptConsequence.getTranscriptId())
                        );
                    }
                }

            }
        }

    }

    public static void importOmim(Driver driver){

        try {

            String inputLine;
            URL omimFtp = new URL("http://data.omim.org/downloads/NFUI_mdqQbaQADxKesNmgg/morbidmap.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(omimFtp.openStream()));

            while ((inputLine = in.readLine()) != null){

                if (Pattern.matches("^#.*", inputLine)){
                    continue;
                }

                //split fields
                String[] fields = inputLine.split("\t");
                String disorder = fields[0].trim();

                log.log(Level.INFO,"Importing omim disorder: " + disorder);

                //loop over symbols
                for (String field : fields[1].split(",")) {
                    String symbolId = field.trim();

                    try (Session session = driver.session()) {
                        session.run(
                                "MERGE (d:Disorder {disorder:{disorder}}) MERGE (s:Symbol {symbolId:{symbolId}}) CREATE UNIQUE (d)-[:HAS_ASSOCIATED_SYMBOL]->(s);",
                                Values.parameters(
                                        "disorder", disorder,
                                        "symbolId", symbolId
                                )
                        );
                    }

                }


            }

            in.close();
        } catch (IOException e){
            log.log(Level.SEVERE,"Could not connect to OMIM: " + e.getMessage());
        }

    }

    public static void removeOmim(Driver driver){
        try (Session session = driver.session()) {
            session.run("MATCH (d:Disorder)-[rel]-(s) DELETE d,rel;");
        }
    }

    public static void removeClinvar(Driver driver){
        try (Session session = driver.session()) {
            session.run("MATCH (v:Variant) REMOVE v.clinvar;");
        }
    }

    public static void importClinvar(Driver driver) {
        VCFCodec codec = new VCFCodec();

        //read VCF from clinvar, decompress and import on the fly
        try (LineIteratorImpl lineIteratorImpl = new LineIteratorImpl(LineReaderUtil.fromBufferedStream(new GZIPInputStream(new URL("ftp://ftp.ncbi.nlm.nih.gov/pub/clinvar/vcf_GRCh37/clinvar.vcf.gz").openStream())))) {
            codec.readActualHeader(lineIteratorImpl);

            while(lineIteratorImpl.hasNext()) {
                VariantContext variantContext = codec.decode(lineIteratorImpl.next());

                if (variantContext.getAttribute("CLNALLE") instanceof String) {
                    if (variantContext.getAttribute("CLNSIG") == null) continue; //not always present

                    int clinAllele = Integer.parseInt((String) variantContext.getAttribute("CLNALLE"));
                    if (clinAllele == -1) continue; //A value of -1 indicates that no allele was found to match a corresponding HGVS allele name
                    String clinSig = (String) variantContext.getAttribute("CLNSIG");

                    GenomeVariant genomeVariant = new GenomeVariant(variantContext.getContig(), variantContext.getStart(), variantContext.getReference().getBaseString(), variantContext.getAlleles().get(clinAllele).getBaseString());
                    genomeVariant.convertToMinimalRepresentation();

                    String[] clinSigsString = clinSig.split("\\|");
                    int[] clinSigsInt = new int[clinSigsString.length];

                    for (int i = 0; i < clinSigsString.length; ++i){
                        clinSigsInt[i] = Integer.parseInt(clinSigsString[i]);
                    }

                    log.log(Level.INFO,"Importing clinvar annotation: " + genomeVariant.toString());
                    try (Session session = driver.session()) {
                        session.run("MERGE (v:Variant {variantId:{variantId}}) ON CREATE SET v :Annotate SET v.clinvar = {clinvar};",
                                Values.parameters(
                                        "variantId",genomeVariant.toString(),
                                        "clinvar", clinSigsInt
                                )
                        );
                    }

                } else if (variantContext.getAttribute("CLNALLE") instanceof ArrayList) {
                    if (variantContext.getAttribute("CLNSIG") == null) continue; //not always present

                    ArrayList<String> clinSigs = (ArrayList<String>) variantContext.getAttribute("CLNSIG");
                    ArrayList<String> clinAlleles = (ArrayList<String>) variantContext.getAttribute("CLNALLE");

                    for (int n = 0; n < clinAlleles.size(); ++n){

                        int clinAllele = Integer.parseInt(clinAlleles.get(n));
                        if (clinAllele == -1) continue; //A value of -1 indicates that no allele was found to match a corresponding HGVS allele name

                        GenomeVariant genomeVariant = new GenomeVariant(variantContext.getContig(), variantContext.getStart(), variantContext.getReference().getBaseString(), variantContext.getAlleles().get(clinAllele).getBaseString());
                        genomeVariant.convertToMinimalRepresentation();

                        String[] clinSigsString = clinSigs.get(n).split("\\|");
                        int[] clinSigsInt = new int[clinSigsString.length];

                        for (int i = 0; i < clinSigsString.length; ++i){
                            clinSigsInt[i] = Integer.parseInt(clinSigsString[i]);
                        }

                        log.log(Level.INFO,"Importing clinvar annotation: " + genomeVariant.toString());
                        try (Session session = driver.session()) {
                            session.run("MERGE (v:Variant {variantId:{variantId}, clinvar:{clinvar}}) ON CREATE SET v :Annotate ",
                                    Values.parameters(
                                            "variantId",genomeVariant.toString(),
                                            "clinvar", clinSigsInt
                                    )
                            );
                        }

                    }

                }

            }

        } catch (IOException e) {
            log.log(Level.SEVERE,"Problem reading clinvar VCF: " + e.getMessage());
        }

    }

    //helper functions
    private GenomeVariant convertVepInputToGenomeVariant(String input){
        String[] fields = input.split("\t");
        return new GenomeVariant(fields[0], Integer.parseInt(fields[1]), fields[3], fields[4]);
    }
    private static boolean filterTranscriptConsequences(TranscriptConsequence transcriptConsequence) {

        //check biotype
        if (transcriptConsequence.getBiotype() == null || !transcriptConsequence.getBiotype().equals("protein_coding")) {
            return true;
        }

        //check nm no
        if (transcriptConsequence.getTranscriptId() == null || !transcriptConsequence.getTranscriptId().startsWith("NM_")) {
            return true;
        }

        return false;
    }

}
