package nhs.genetics.cardiff;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A POJO for Ensembl VEP JSON output
 *
 * @author  Matt Lyon
 * @version 1.0
 * @since   2016-05-09
 */
public class RegulatoryFeatureConsequence {

    @JsonProperty("regulatory_feature_id")
    private String regulatoryFeatureId;

    @JsonProperty("variant_allele")
    private String variantAllele;

    @JsonProperty("consequence_terms")
    private String[] consequenceTerms;

    private String biotype,impact;
    private Double phylop, gerp, phastcons;

    public RegulatoryFeatureConsequence(){

    }

    public Double getGerp() {
        return gerp;
    }

    public void setGerp(Double gerp) {
        this.gerp = gerp;
    }

    public Double getPhylop() {
        return phylop;
    }

    public void setPhylop(Double phylop) {
        this.phylop = phylop;
    }

    public String getRegulatoryFeatureId() {
        return regulatoryFeatureId;
    }

    public void setRegulatoryFeatureId(String regulatoryFeatureId) {
        this.regulatoryFeatureId = regulatoryFeatureId;
    }

    public String getVariantAllele() {
        return variantAllele;
    }

    public void setVariantAllele(String variantAllele) {
        this.variantAllele = variantAllele;
    }

    public String[] getConsequenceTerms() {
        return consequenceTerms;
    }

    public void setConsequenceTerms(String[] consequenceTerms) {
        this.consequenceTerms = consequenceTerms;
    }

    public String getBiotype() {
        return biotype;
    }

    public void setBiotype(String biotype) {
        this.biotype = biotype;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }
}
