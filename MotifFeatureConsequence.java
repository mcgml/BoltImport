package nhs.genetics.cardiff;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ml on 10/05/2016.
 */
public class MotifFeatureConsequence {

    @JsonProperty("consequence_terms")
    private String[] consequenceTerms;

    @JsonProperty("variant_allele")
    private String variantAllele;

    @JsonProperty("high_inf_pos")
    private String highInfPos;

    @JsonProperty("motif_feature_id")
    private String motifFeatureId;

    @JsonProperty("motif_name")
    private String motifName;

    @JsonProperty("motif_score_change")
    private Double motifScoreChange;

    @JsonProperty("motif_pos")
    private Integer motifPos;

    private String impact;
    private Double phylop, phastcons, gerp;
    private Integer strand;

    public MotifFeatureConsequence(){

    }

    public String[] getConsequenceTerms() {
        return consequenceTerms;
    }

    public void setConsequenceTerms(String[] consequenceTerms) {
        this.consequenceTerms = consequenceTerms;
    }

    public String getVariantAllele() {
        return variantAllele;
    }

    public void setVariantAllele(String variantAllele) {
        this.variantAllele = variantAllele;
    }

    public String getHighInfPos() {
        return highInfPos;
    }

    public void setHighInfPos(String highInfPos) {
        this.highInfPos = highInfPos;
    }

    public String getMotifFeatureId() {
        return motifFeatureId;
    }

    public void setMotifFeatureId(String motifFeatureId) {
        this.motifFeatureId = motifFeatureId;
    }

    public String getMotifName() {
        return motifName;
    }

    public void setMotifName(String motifName) {
        this.motifName = motifName;
    }

    public Double getMotifScoreChange() {
        return motifScoreChange;
    }

    public void setMotifScoreChange(Double motifScoreChange) {
        this.motifScoreChange = motifScoreChange;
    }

    public Integer getMotifPos() {
        return motifPos;
    }

    public void setMotifPos(Integer motifPos) {
        this.motifPos = motifPos;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public Integer getStrand() {
        return strand;
    }

    public void setStrand(Integer strand) {
        this.strand = strand;
    }

    public Double getPhylop() {
        return phylop;
    }

    public void setPhylop(Double phylop) {
        this.phylop = phylop;
    }

    public Double getPhastcons() {
        return phastcons;
    }

    public void setPhastcons(Double phastcons) {
        this.phastcons = phastcons;
    }

    public Double getGerp() {
        return gerp;
    }

    public void setGerp(Double gerp) {
        this.gerp = gerp;
    }
}
