package nhs.genetics.cardiff;

import nhs.genetics.cardiff.framework.GenomeVariant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * A wrapper for annotating variants using Mutation Taster. Requires internet connection.
 *
 * @author  Matt Lyon
 * @version 1.0
 * @since   2016-05-09
 */
//TODO add support for indels
public class MutationTaster {
    private static final Logger log = Logger.getLogger(MutationTaster.class.getName());

    private Double pValue;
    private String prediction, gene, transcript, hgvsc, hgvsp;
    private final static File dbnsfpFile = new File("dbNSFP.gz");
    private GenomeVariant genomeVariant;

    public MutationTaster(String gene, String transcript, String hgvsc, String hgvsp) {
        this.gene = gene;
        this.transcript = transcript;
        this.hgvsc = hgvsc;
        this.hgvsp = hgvsp;
    }

    public MutationTaster(GenomeVariant genomeVariant) {
        this.genomeVariant = genomeVariant;
    }

    public void queryOnlineMutationTasterUsingHgvs() throws IOException {

        int count = 0;
        int maxTries = 3;

        while (true) {

            try {

                //check HGVS is coding SNP
                if (hgvsc == null || hgvsp == null || !Pattern.matches("^c\\.[0-9]+[AGTCagct]>[AGTCagct]", hgvsc) || hgvsp.equals("p.=")){
                    return;
                }

                //extract fields
                String altAllele = hgvsc.split(">")[1].toUpperCase();
                Integer codingPosition = Integer.parseInt(hgvsc.replaceAll("\\D+", ""));

                Document doc = Jsoup.connect("http://www.mutationtaster.org/cgi-bin/MutationTaster/MutationTaster69.cgi")
                        .data("gene", gene, "transcript_stable_id", transcript, "sequence_type", "CDS", "position_be", String.valueOf(codingPosition), "new_base", altAllele)
                        .post();

                //get prediction
                for (Element element : doc.getElementsByTag("input")) {
                    if (element.attr("name").equals("prediction")) {
                        prediction = element.attr("value");
                    }
                }

                //get Pvalue
                for (Element tdElement : doc.getElementsByTag("td")) {
                    if (tdElement.toString().contains("Model")){
                        String[] fields = tdElement.toString().split(" ");
                        for (int n = 0; n < fields.length; ++n){
                            if (fields[n].equals("prob:")){
                                pValue = Double.parseDouble(fields[n + 1]);
                                break;
                            }
                        }
                    }
                }

                return;
            } catch (IOException e) {
                if (++count == maxTries) {
                    throw e;
                }
            } catch (NullPointerException e){
                log.log(Level.SEVERE, "Problem with record: " + gene + " " + transcript + " " + hgvsc + " " + hgvsp);
                throw e;
            }

        }

    }

    public void queryOnlineMutationTasterUsingChromPos() throws IOException {

        int count = 0;
        int maxTries = 3;

        while (true) {

            try {

                Document doc = Jsoup.connect("http://www.mutationtaster.org/cgi-bin/MutationTaster/MT_ChrPos.cgi")
                        .data("chromosome", genomeVariant.getContig(), "position", String.valueOf(genomeVariant.getPos()), "ref", genomeVariant.getRef(), "alt", genomeVariant.getAlt())
                        .post();

                System.out.println(doc.toString());

                return;
            } catch (IOException e) {
                if (++count == maxTries) {
                    throw e;
                }
            } catch (NullPointerException e){
                log.log(Level.SEVERE, "Problem with record: " + genomeVariant);
                throw e;
            }

        }

    }

    public Double getpValue() {
        return pValue;
    }
    public String getPrediction() {
        return prediction;
    }
}
