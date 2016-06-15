package nhs.genetics.cardiff;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Programme for importing VCF files and annotations into a Neo4j database
 *
 * @author  Matt Lyon
 * @version 1.0
 * @since   2016-05-09
 */

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    private static final String version = "1.0.1";
    private static File variantVcfFile = null, blacklistVcfFile = null;
    private static boolean annotateQueuedVariants = false;
    private static Integer threads;

    private static void printCommandLineArguments(Boolean printProgramme){
        if (printProgramme) System.err.println("BoltImport v" + version);
        if (printProgramme) System.err.println("Description: Programme for importing variants into a Neo4j database");
        if (printProgramme) System.err.println("Author: Matt Lyon, Institute of Medical Genetics, Wales, UK\n");
        System.err.println("Usage:");
        System.err.println("-a      Annotate");
        System.err.println("-b FILE Exclude variants from VCF [null]");
        System.err.println("-i FILE Import variants from VCF [null]");
        System.err.println("-t INT  Threads [1]");
    }

    public static void main(String[] args) throws IOException {

        //check command line args
        if (args.length < 1 || args.length > 7) {
            printCommandLineArguments(true);
            throw new RuntimeException("Check command-line arguments");
        }

        //loop over arguments
        try {
            for (int n = 0; n < args.length; ++n){
                switch (args[n]) {
                    case "-a":
                        annotateQueuedVariants = true;
                        break;
                    case "-i":
                        variantVcfFile = new File(args[n+1]);
                        break;
                    case "-b":
                        blacklistVcfFile = new File(args[n+1]);
                        break;
                    case "-t":
                        threads = Integer.parseInt(args[n+1]);
                        break;
                }
            }
        } catch (Exception e){
            printCommandLineArguments(false);
            throw new RuntimeException("Check command-line arguments: " + e.getMessage());
        }

        final Driver driver = GraphDatabase.driver("bolt://localhost");

        //import variants
        if (variantVcfFile != null){
            try {

                ImportVariants importVariants = new ImportVariants(variantVcfFile, driver);

                importVariants.parseVCFMetaData();
                importVariants.addSamplesAndAnalyses();
                if (blacklistVcfFile != null) importVariants.setBlackList(blacklistVcfFile);

                importVariants.addVariants();

            } catch (Exception e) {
                log.log(Level.SEVERE, e.getMessage());
                return;
            }
        }

        //annotate variants
        if (annotateQueuedVariants){
            try {

                VEPWrapper vepWrapper = new VEPWrapper(ImportAnnotations.getAnnotationQueue(driver), new File("variant_effect_predictor.pl"));
                if (threads != null) vepWrapper.setThreads(threads);
                vepWrapper.run();

                //import annotations
                ImportAnnotations importAnnotations = new ImportAnnotations(vepWrapper.getVepRecords(), driver);
                importAnnotations.importVEPRecords();

            } catch (Exception e){
                log.log(Level.SEVERE, e.getMessage());
                return;
            }
        }

        driver.close();
    }

}
