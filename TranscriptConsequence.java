package nhs.genetics.cardiff;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A POJO for Ensembl VEP JSON output
 *
 * @author  Matt Lyon
 * @version 1.0
 * @since   2016-05-09
 */
public class TranscriptConsequence {

    @JsonProperty("gene_id")
    private String geneId;

    @JsonProperty("gene_symbol")
    private String geneSymbol;

    @JsonProperty("transcript_id")
    private String transcriptId;

    @JsonProperty("protein_id")
    private String proteinId;

    @JsonProperty("gene_symbol_source")
    private String geneSymbolSource;

    @JsonProperty("variant_allele")
    private String variantAllele;

    @JsonProperty("amino_acids")
    private String aminoAcids;

    @JsonProperty("polyphen_prediction")
    private String polyphenPrediction;

    @JsonProperty("cdna_end")
    private Integer cdnaEnd;

    @JsonProperty("hgnc_id")
    private Integer hgncId;

    @JsonProperty("protein_end")
    private Integer proteinEnd;

    @JsonProperty("cds_end")
    private Integer cdsEnd;

    @JsonProperty("cds_start")
    private Integer cdsStart;

    @JsonProperty("cdna_start")
    private Integer cdnaStart;

    @JsonProperty("protein_start")
    private Integer proteinStart;

    @JsonProperty("hgvs_offset")
    private Integer hgvsOffset;

    @JsonProperty("consequence_terms")
    private String[] consequenceTerms;

    @JsonProperty("gene_pheno")
    private Integer genePheno;

    @JsonProperty("polyphen_score")
    private Double polyphenScore;

    @JsonProperty("sift_score")
    private Double siftScore;

    @JsonProperty("sift_prediction")
    private String siftPrediction;

    private String trembl, exon, swissprot, biotype, codons, impact, hgvsc, ccds, hgvsp, uniparc, intron, flags;
    private Integer distance, strand;
    private Domain[] domains;
    private Boolean canonical;
    private Double gerp, phylop, phastcons;

    public TranscriptConsequence(){

    }

    public String getHgvsc() {
        try {
            return hgvsc.split(":")[1];
        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new RuntimeException("Could not split HGVSc field: " + hgvsc);
        }
    }
    public String getHgvsp() {
        try {
            String h = hgvsp.split(":")[1];
            if (h.contains("(p.=)")){
                return "p.=";
            } else {
                return h;
            }
        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new RuntimeException("Could not split HGVSp field: " + hgvsp);
        }
    }
    public String getExonOrIntronNumber(){
        if (exon != null){
            return exon.split("/")[0];
        } else if (intron != null){
            return intron.split("/")[0];
        } else {
            return null;
        }
    }

    public String getGeneId() {
        return geneId;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getTranscriptId() {
        return transcriptId;
    }

    public void setTranscriptId(String transcriptId) {
        this.transcriptId = transcriptId;
    }

    public String getProteinId() {
        return proteinId;
    }

    public void setProteinId(String proteinId) {
        this.proteinId = proteinId;
    }

    public String getGeneSymbolSource() {
        return geneSymbolSource;
    }

    public void setGeneSymbolSource(String geneSymbolSource) {
        this.geneSymbolSource = geneSymbolSource;
    }

    public String getVariantAllele() {
        return variantAllele;
    }

    public void setVariantAllele(String variantAllele) {
        this.variantAllele = variantAllele;
    }

    public String getAminoAcids() {
        return aminoAcids;
    }

    public void setAminoAcids(String aminoAcids) {
        this.aminoAcids = aminoAcids;
    }

    public String getPolyphenPrediction() {
        return polyphenPrediction;
    }

    public void setPolyphenPrediction(String polyphenPrediction) {
        this.polyphenPrediction = polyphenPrediction;
    }

    public Integer getCdnaEnd() {
        return cdnaEnd;
    }

    public void setCdnaEnd(Integer cdnaEnd) {
        this.cdnaEnd = cdnaEnd;
    }

    public Integer getHgncId() {
        return hgncId;
    }

    public void setHgncId(Integer hgncId) {
        this.hgncId = hgncId;
    }

    public Integer getProteinEnd() {
        return proteinEnd;
    }

    public void setProteinEnd(Integer proteinEnd) {
        this.proteinEnd = proteinEnd;
    }

    public Integer getCdsEnd() {
        return cdsEnd;
    }

    public void setCdsEnd(Integer cdsEnd) {
        this.cdsEnd = cdsEnd;
    }

    public Integer getCdsStart() {
        return cdsStart;
    }

    public void setCdsStart(Integer cdsStart) {
        this.cdsStart = cdsStart;
    }

    public Integer getCdnaStart() {
        return cdnaStart;
    }

    public void setCdnaStart(Integer cdnaStart) {
        this.cdnaStart = cdnaStart;
    }

    public Integer getProteinStart() {
        return proteinStart;
    }

    public void setProteinStart(Integer proteinStart) {
        this.proteinStart = proteinStart;
    }

    public Integer getHgvsOffset() {
        return hgvsOffset;
    }

    public void setHgvsOffset(Integer hgvsOffset) {
        this.hgvsOffset = hgvsOffset;
    }

    public String[] getConsequenceTerms() {
        return consequenceTerms;
    }

    public void setConsequenceTerms(String[] consequenceTerms) {
        this.consequenceTerms = consequenceTerms;
    }

    public Integer getGenePheno() {
        return genePheno;
    }

    public void setGenePheno(Integer genePheno) {
        this.genePheno = genePheno;
    }

    public Double getPolyphenScore() {
        return polyphenScore;
    }

    public void setPolyphenScore(Double polyphenScore) {
        this.polyphenScore = polyphenScore;
    }

    public Double getSiftScore() {
        return siftScore;
    }

    public void setSiftScore(Double siftScore) {
        this.siftScore = siftScore;
    }

    public String getSiftPrediction() {
        return siftPrediction;
    }

    public void setSiftPrediction(String siftPrediction) {
        this.siftPrediction = siftPrediction;
    }

    public String getTrembl() {
        return trembl;
    }

    public void setTrembl(String trembl) {
        this.trembl = trembl;
    }

    public void setExon(String exon) {
        this.exon = exon;
    }

    public String getSwissprot() {
        return swissprot;
    }

    public void setSwissprot(String swissprot) {
        this.swissprot = swissprot;
    }

    public String getBiotype() {
        return biotype;
    }

    public void setBiotype(String biotype) {
        this.biotype = biotype;
    }

    public String getCodons() {
        return codons;
    }

    public void setCodons(String codons) {
        this.codons = codons;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public void setHgvsc(String hgvsc) {
        this.hgvsc = hgvsc;
    }

    public String getCcds() {
        return ccds;
    }

    public void setCcds(String ccds) {
        this.ccds = ccds;
    }

    public void setHgvsp(String hgvsp) {
        this.hgvsp = hgvsp;
    }

    public String getUniparc() {
        return uniparc;
    }

    public void setUniparc(String uniparc) {
        this.uniparc = uniparc;
    }

    public void setIntron(String intron) {
        this.intron = intron;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getStrand() {
        return strand;
    }

    public void setStrand(Integer strand) {
        this.strand = strand;
    }

    public Domain[] getDomains() {
        return domains;
    }

    public void setDomains(Domain[] domains) {
        this.domains = domains;
    }

    public Boolean getCanonical() {
        return canonical;
    }

    public void setCanonical(Boolean canonical) {
        this.canonical = canonical;
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

    public Double getPhastcons() {
        return phastcons;
    }

    public void setPhastcons(Double phastcons) {
        this.phastcons = phastcons;
    }
}
