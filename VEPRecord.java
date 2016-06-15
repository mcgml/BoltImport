package nhs.genetics.cardiff;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A POJO for Ensembl VEP JSON output
 *
 * @author  Matt Lyon
 * @version 1.0
 * @since   2016-05-09
 */
public class VEPRecord {

    @JsonProperty("assembly_name")
    private String assemblyName;

    @JsonProperty("variant_class")
    private String variantClass;

    @JsonProperty("seq_region_name")
    private String seqRegionName;

    @JsonProperty("allele_string")
    private String alleleString;

    @JsonProperty("most_severe_consequence")
    private String mostSevereConsequence;

    @JsonProperty("regulatory_feature_consequences")
    private RegulatoryFeatureConsequence[] regulatoryFeatureConsequences;

    @JsonProperty("colocated_variants")
    private ColocatedVariant[] colocatedVariants;

    @JsonProperty("transcript_consequences")
    private TranscriptConsequence[] transcriptConsequences;

    @JsonProperty("motif_feature_consequences")
    private MotifFeatureConsequence[] motifFeatureConsequences;

    private String input, id;
    private int strand, start, end;

    public VEPRecord() {

    }

    public MotifFeatureConsequence[] getMotifFeatureConsequences() {
        return motifFeatureConsequences;
    }

    public void setMotifFeatureConsequences(MotifFeatureConsequence[] motifFeatureConsequences) {
        this.motifFeatureConsequences = motifFeatureConsequences;
    }

    public String getAssemblyName() {
        return assemblyName;
    }

    public void setAssemblyName(String assemblyName) {
        this.assemblyName = assemblyName;
    }

    public String getVariantClass() {
        return variantClass;
    }

    public void setVariantClass(String variantClass) {
        this.variantClass = variantClass;
    }

    public String getSeqRegionName() {
        return seqRegionName;
    }

    public void setSeqRegionName(String seqRegionName) {
        this.seqRegionName = seqRegionName;
    }

    public String getAlleleString() {
        return alleleString;
    }

    public void setAlleleString(String alleleString) {
        this.alleleString = alleleString;
    }

    public String getMostSevereConsequence() {
        return mostSevereConsequence;
    }

    public void setMostSevereConsequence(String mostSevereConsequence) {
        this.mostSevereConsequence = mostSevereConsequence;
    }

    public RegulatoryFeatureConsequence[] getRegulatoryFeatureConsequences() {
        return regulatoryFeatureConsequences;
    }

    public void setRegulatoryFeatureConsequences(RegulatoryFeatureConsequence[] regulatoryFeatureConsequences) {
        this.regulatoryFeatureConsequences = regulatoryFeatureConsequences;
    }

    public ColocatedVariant getColocatedVariants() {
        try {
            return colocatedVariants[0];
        } catch (NullPointerException e){
            return null;
        }
    }

    public void setColocatedVariants(ColocatedVariant[] colocatedVariants) {
        this.colocatedVariants = colocatedVariants;
    }

    public TranscriptConsequence[] getTranscriptConsequences() {
        return transcriptConsequences;
    }

    public void setTranscriptConsequences(TranscriptConsequence[] transcriptConsequences) {
        this.transcriptConsequences = transcriptConsequences;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStrand() {
        return strand;
    }

    public void setStrand(int strand) {
        this.strand = strand;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
