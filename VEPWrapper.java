package nhs.genetics.cardiff;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhs.genetics.cardiff.framework.GenomeVariant;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A wrapper for running Ensembl VEP
 *
 * @author  Matt Lyon
 * @version 1.0
 * @since   2016-05-09
 */
public class VEPWrapper {
    private static final Logger log = Logger.getLogger(VEPWrapper.class.getName());

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private ArrayList<VEPRecord> vepRecords = new ArrayList<>();
    private ArrayList<GenomeVariant> genomeVariants;
    private File scriptFile;
    private Integer threads = 1;

    public VEPWrapper(ArrayList<GenomeVariant> genomeVariants, File scriptFile){
        this.genomeVariants = genomeVariants;
        this.scriptFile = scriptFile;
    }

    /**
     * @throws IOException Could not deserialize VEP output
     * @throws InterruptedException Could not run VEP
     * */
    public void run() throws IOException, InterruptedException {
        log.log(Level.INFO, "Annotating variant(s) with " + threads + " threads ...");

        try {

            ProcessBuilder builder = new ProcessBuilder(
                    "perl",
                    scriptFile.toString(),
                    "--cache",
                    "--offline",
                    "--refseq",
                    "--everything",
                    "--species",
                    "homo_sapiens",
                    "--assembly",
                    "GRCh37",
                    "--cache_version",
                    "84",
                    "--format",
                    "vcf",
                    "--json",
                    "--no_stats",
                    "--no_escape",
                    "--no_intergenic",
                    "--fork",
                    String.valueOf(threads),
                    "-o",
                    "STDOUT"
            );

            Process process = builder.start();

            OutputStream stdin = process.getOutputStream();
            InputStream stdout = process.getInputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

            writer.write(convertGenomeVariantListToVcf());
            writer.flush();
            writer.close();

            Scanner scanner = new Scanner(stdout);
            while (scanner.hasNextLine()) {
                vepRecords.add(objectMapper.readValue(scanner.nextLine(), VEPRecord.class)); //convert JSON to POJO
            }

            if (process.waitFor() != 0){
                throw new RuntimeException("Problem invoking vep, exit code: " + process.exitValue());
            }

        } catch (IOException e){
            log.log(Level.SEVERE, "Could not deserialize VEP output: " + e.toString());
            throw e;
        } catch (InterruptedException e){
            log.log(Level.SEVERE, "Could not execute VEP: " + e.toString());
            throw e;
        }

    }

    private String convertGenomeVariantListToVcf(){

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("##fileformat=VCFv4.1\n");
        stringBuilder.append("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\n");

        for (GenomeVariant genomeVariant : genomeVariants){
            stringBuilder.append(genomeVariant.getContig());
            stringBuilder.append("\t");
            stringBuilder.append(genomeVariant.getPos());
            stringBuilder.append("\t.\t");
            stringBuilder.append(genomeVariant.getRef());
            stringBuilder.append("\t");
            stringBuilder.append(genomeVariant.getAlt());
            stringBuilder.append("\t.\t.\t.\n");
        }

        return stringBuilder.toString();
    }

    public ArrayList<VEPRecord> getVepRecords() {
        return vepRecords;
    }
    public void setThreads(Integer threads) {
        this.threads = threads;
    }
}
