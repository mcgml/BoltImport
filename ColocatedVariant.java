package nhs.genetics.cardiff;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A POJO for Ensembl VEP JSON output
 *
 * @author  Matt Lyon
 * @version 1.0
 * @since   2016-05-09
 */

public class ColocatedVariant {

    /*
    * ## GMAF : Minor allele and frequency of existing variant in 1000 Genomes combined population
    * ## AFR_MAF : Frequency of existing variant in 1000 Genomes combined African population
    * ## AMR_MAF : Frequency of existing variant in 1000 Genomes combined American population
    * ## EAS_MAF : Frequency of existing variant in 1000 Genomes combined East Asian population
    * ## EUR_MAF : Frequency of existing variant in 1000 Genomes combined European population
    * ## SAS_MAF : Frequency of existing variant in 1000 Genomes combined South Asian population
    * ## AA_MAF : Frequency of existing variant in NHLBI-ESP African American population
    * ## EA_MAF : Frequency of existing variant in NHLBI-ESP European American population
    * ## ExAC_MAF : Frequency of existing variant in ExAC combined population
    * ## ExAC_Adj_MAF : Adjusted frequency of existing variant in ExAC combined population
    * ## ExAC_AFR_MAF : Frequency of existing variant in ExAC African/American population
    * ## ExAC_AMR_MAF : Frequency of existing variant in ExAC American population
    * ## ExAC_EAS_MAF : Frequency of existing variant in ExAC East Asian population
    * ## ExAC_FIN_MAF : Frequency of existing variant in ExAC Finnish population
    * ## ExAC_NFE_MAF : Frequency of existing variant in ExAC Non-Finnish European population
    * ## ExAC_OTH_MAF : Frequency of existing variant in ExAC combined other combined populations
    * ## ExAC_SAS_MAF : Frequency of existing variant in ExAC South Asian population
    */

    /**
     * 1kg alleles
     */

    @JsonProperty("eur_allele")
    private String eur1kgAllele;

    @JsonProperty("sas_allele")
    private String sas1kgAllele;

    @JsonProperty("eas_allele")
    private String eas1kgAllele;

    @JsonProperty("afr_allele")
    private String afr1kgAllele;

    @JsonProperty("amr_allele")
    private String amr1kgAllele;

    @JsonProperty("eas_maf")
    private String eas1kgMafString; //Frequency of existing variant in 1000 Genomes combined East Asian population

    @JsonProperty("afr_maf")
    private String afr1kgMafString; //Frequency of existing variant in 1000 Genomes combined African population

    @JsonProperty("amr_maf")
    private String amr1kgMafString; //Frequency of existing variant in 1000 Genomes combined American population

    @JsonProperty("sas_maf")
    private String sas1kgMafString; //Frequency of existing variant in 1000 Genomes combined South Asian population

    @JsonProperty("eur_maf")
    private String eur1kgMafString; //Frequency of existing variant in 1000 Genomes combined European population

    /**
     * esp alleles
     */

    @JsonProperty("ea_allele")
    private String eaAllele;

    @JsonProperty("aa_allele")
    private String aaAllele;

    @JsonProperty("aa_maf")
    private String aaEspMafString; //Frequency of existing variant in NHLBI-ESP African American population

    @JsonProperty("ea_maf")
    private String eaEspMafString; //Frequency of existing variant in NHLBI-ESP European American population

    /**
     * exac alleles
     */

    @JsonProperty("exac_allele")
    private String exacAllele;

    @JsonProperty("exac_adj_allele")
    private String exacAdjAllele;

    @JsonProperty("exac_nfe_allele")
    private String exacNfeAllele; //Frequency of existing variant in ExAC Non-Finnish European population

    @JsonProperty("exac_sas_allele")
    private String exacSasAllele; //Frequency of existing variant in ExAC South Asian population

    @JsonProperty("exac_afr_allele")
    private String exacAfrAllele; //Frequency of existing variant in ExAC African/American population

    @JsonProperty("exac_eas_allele")
    private String exacEasAllele; //Frequency of existing variant in ExAC East Asian population

    @JsonProperty("exac_amr_allele")
    private String exacAmrAllele; //Frequency of existing variant in ExAC American population

    @JsonProperty("exac_oth_allele")
    private String exacOthAllele; //Frequency of existing variant in ExAC combined other combined populations

    @JsonProperty("exac_fin_allele")
    private String exacFinAllele; //Frequency of existing variant in ExAC Finnish population

    @JsonProperty("exac_nfe_maf")
    private String exacNfeMafString; //Frequency of existing variant in ExAC Non-Finnish European population

    @JsonProperty("exac_sas_maf")
    private String exacSasMafString; //Frequency of existing variant in ExAC South Asian population

    @JsonProperty("exac_afr_maf")
    private String exacAfrMafString; //Frequency of existing variant in ExAC African/American population

    @JsonProperty("exac_eas_maf")
    private String exacEasMafString; //Frequency of existing variant in ExAC East Asian population

    @JsonProperty("exac_amr_maf")
    private String exacAmrMafString; //Frequency of existing variant in ExAC American population

    @JsonProperty("exac_oth_maf")
    private String exacOthMafString; //Frequency of existing variant in ExAC combined other combined populations

    @JsonProperty("exac_fin_maf")
    private String exacFinMafString; //Frequency of existing variant in ExAC Finnish population

    @JsonProperty("exac_maf")
    private String exacMafString; //Frequency of existing variant in ExAC combined

    @JsonProperty("exac_adj_maf")
    private String exacAdjMafString; //Frequency of existing variant in ExAC combined

    @JsonProperty("minor_allele_freq")
    private String minorAlleleFreq;

    @JsonProperty("minor_allele")
    private String minorAllele;

    @JsonProperty("clin_sig")
    private String[] clinSig;

    @JsonProperty("phenotype_or_disease")
    private Boolean phenotypeOrDisease;

    @JsonProperty("allele_string")
    private String alleleString;

    private String id, dbSnpId, cosmicId, hgmdId;
    private Integer[] pubmed;
    private Integer strand, start, end;
    private Boolean somatic;
    private Double eas1kgMaf, afr1kgMaf, amr1kgMaf, sas1kgMaf, eur1kgMaf, aaEspMaf, eaEspMaf,
            exacNfeMaf, exacSasMaf, exacAfrMaf, exacEasMaf, exacAmrMaf, exacOthMaf, exacFinMaf, exacAdjMaf;

    public ColocatedVariant() {

    }
    public void splitIdField() {

        if (id.matches("^rs\\d*")) {
            dbSnpId = id;
        }

        if (id.matches("^COSM\\d*")) {
            cosmicId = id;
        }

        if (id.matches("^CM\\d*")) {
            hgmdId = id;
        }

    }
    public void setMafs(){

        this.eas1kgMaf = parseMaf(eas1kgMafString);
        this.afr1kgMaf = parseMaf(afr1kgMafString);
        this.amr1kgMaf = parseMaf(amr1kgMafString);
        this.sas1kgMaf = parseMaf(sas1kgMafString);
        this.eur1kgMaf = parseMaf(eur1kgMafString);

        this.aaEspMaf = parseMaf(aaEspMafString);
        this.eaEspMaf = parseMaf(eaEspMafString);

        this.exacNfeMaf = parseMaf(exacNfeMafString);
        this.exacSasMaf = parseMaf(exacSasMafString);
        this.exacAfrMaf = parseMaf(exacAfrMafString);
        this.exacEasMaf = parseMaf(exacEasMafString);
        this.exacAmrMaf = parseMaf(exacAmrMafString);
        this.exacOthMaf = parseMaf(exacOthMafString);
        this.exacFinMaf = parseMaf(exacFinMafString);
        this.exacAdjMaf = parseMaf(exacAdjMafString);

    }
    private static Double parseMaf(String maf){
        try {
            return Double.parseDouble(maf);
        } catch (NullPointerException e){
            return null;
        } catch (NumberFormatException e){
            try {
                return Double.parseDouble(maf.replace("-:", ""));
            } catch (NumberFormatException e1){
                return null;
            }
        }
    }

    public String getEur1kgAllele() {
        return eur1kgAllele;
    }

    public void setEur1kgAllele(String eur1kgAllele) {
        this.eur1kgAllele = eur1kgAllele;
    }

    public String getSas1kgAllele() {
        return sas1kgAllele;
    }

    public void setSas1kgAllele(String sas1kgAllele) {
        this.sas1kgAllele = sas1kgAllele;
    }

    public String getEas1kgAllele() {
        return eas1kgAllele;
    }

    public void setEas1kgAllele(String eas1kgAllele) {
        this.eas1kgAllele = eas1kgAllele;
    }

    public String getAfr1kgAllele() {
        return afr1kgAllele;
    }

    public void setAfr1kgAllele(String afr1kgAllele) {
        this.afr1kgAllele = afr1kgAllele;
    }

    public String getAmr1kgAllele() {
        return amr1kgAllele;
    }

    public void setAmr1kgAllele(String amr1kgAllele) {
        this.amr1kgAllele = amr1kgAllele;
    }

    public void setEas1kgMafString(String eas1kgMafString) {
        this.eas1kgMafString = eas1kgMafString;
    }

    public void setAfr1kgMafString(String afr1kgMafString) {
        this.afr1kgMafString = afr1kgMafString;
    }

    public void setAmr1kgMafString(String amr1kgMafString) {
        this.amr1kgMafString = amr1kgMafString;
    }

    public void setSas1kgMafString(String sas1kgMafString) {
        this.sas1kgMafString = sas1kgMafString;
    }

    public void setEur1kgMafString(String eur1kgMafString) {
        this.eur1kgMafString = eur1kgMafString;
    }

    public String getEaAllele() {
        return eaAllele;
    }

    public void setEaAllele(String eaAllele) {
        this.eaAllele = eaAllele;
    }

    public String getAaAllele() {
        return aaAllele;
    }

    public void setAaAllele(String aaAllele) {
        this.aaAllele = aaAllele;
    }

    public void setAaEspMafString(String aaEspMafString) {
        this.aaEspMafString = aaEspMafString;
    }

    public void setEaEspMafString(String eaEspMafString) {
        this.eaEspMafString = eaEspMafString;
    }

    public String getExacAllele() {
        return exacAllele;
    }

    public void setExacAllele(String exacAllele) {
        this.exacAllele = exacAllele;
    }

    public String getExacAdjAllele() {
        return exacAdjAllele;
    }

    public void setExacAdjAllele(String exacAdjAllele) {
        this.exacAdjAllele = exacAdjAllele;
    }

    public String getExacNfeAllele() {
        return exacNfeAllele;
    }

    public void setExacNfeAllele(String exacNfeAllele) {
        this.exacNfeAllele = exacNfeAllele;
    }

    public String getExacSasAllele() {
        return exacSasAllele;
    }

    public void setExacSasAllele(String exacSasAllele) {
        this.exacSasAllele = exacSasAllele;
    }

    public String getExacAfrAllele() {
        return exacAfrAllele;
    }

    public void setExacAfrAllele(String exacAfrAllele) {
        this.exacAfrAllele = exacAfrAllele;
    }

    public String getExacEasAllele() {
        return exacEasAllele;
    }

    public void setExacEasAllele(String exacEasAllele) {
        this.exacEasAllele = exacEasAllele;
    }

    public String getExacAmrAllele() {
        return exacAmrAllele;
    }

    public void setExacAmrAllele(String exacAmrAllele) {
        this.exacAmrAllele = exacAmrAllele;
    }

    public String getExacOthAllele() {
        return exacOthAllele;
    }

    public void setExacOthAllele(String exacOthAllele) {
        this.exacOthAllele = exacOthAllele;
    }

    public String getExacFinAllele() {
        return exacFinAllele;
    }

    public void setExacFinAllele(String exacFinAllele) {
        this.exacFinAllele = exacFinAllele;
    }

    public void setExacNfeMafString(String exacNfeMafString) {
        this.exacNfeMafString = exacNfeMafString;
    }

    public void setExacSasMafString(String exacSasMafString) {
        this.exacSasMafString = exacSasMafString;
    }

    public void setExacAfrMafString(String exacAfrMafString) {
        this.exacAfrMafString = exacAfrMafString;
    }

    public void setExacEasMafString(String exacEasMafString) {
        this.exacEasMafString = exacEasMafString;
    }

    public void setExacAmrMafString(String exacAmrMafString) {
        this.exacAmrMafString = exacAmrMafString;
    }

    public void setExacOthMafString(String exacOthMafString) {
        this.exacOthMafString = exacOthMafString;
    }

    public void setExacFinMafString(String exacFinMafString) {
        this.exacFinMafString = exacFinMafString;
    }

    public void setExacMafString(String exacMafString) {
        this.exacMafString = exacMafString;
    }

    public void setExacAdjMafString(String exacAdjMafString) {
        this.exacAdjMafString = exacAdjMafString;
    }

    public String getMinorAlleleFreq() {
        return minorAlleleFreq;
    }

    public void setMinorAlleleFreq(String minorAlleleFreq) {
        this.minorAlleleFreq = minorAlleleFreq;
    }

    public String getMinorAllele() {
        return minorAllele;
    }

    public void setMinorAllele(String minorAllele) {
        this.minorAllele = minorAllele;
    }

    public String[] getClinSig() {
        return clinSig;
    }

    public void setClinSig(String[] clinSig) {
        this.clinSig = clinSig;
    }

    public Boolean getPhenotypeOrDisease() {
        return phenotypeOrDisease;
    }

    public void setPhenotypeOrDisease(Boolean phenotypeOrDisease) {
        this.phenotypeOrDisease = phenotypeOrDisease;
    }

    public String getAlleleString() {
        return alleleString;
    }

    public void setAlleleString(String alleleString) {
        this.alleleString = alleleString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDbSnpId() {
        return dbSnpId;
    }

    public void setDbSnpId(String dbSnpId) {
        this.dbSnpId = dbSnpId;
    }

    public String getCosmicId() {
        return cosmicId;
    }

    public void setCosmicId(String cosmicId) {
        this.cosmicId = cosmicId;
    }

    public String getHgmdId() {
        return hgmdId;
    }

    public void setHgmdId(String hgmdId) {
        this.hgmdId = hgmdId;
    }

    public Integer[] getPubmed() {
        return pubmed;
    }

    public void setPubmed(Integer[] pubmed) {
        this.pubmed = pubmed;
    }

    public Integer getStrand() {
        return strand;
    }

    public void setStrand(Integer strand) {
        this.strand = strand;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Boolean getSomatic() {
        return somatic;
    }

    public void setSomatic(Boolean somatic) {
        this.somatic = somatic;
    }

    public Double getEas1kgMaf() {
        return eas1kgMaf;
    }


    public Double getAfr1kgMaf() {
        return afr1kgMaf;
    }


    public Double getAmr1kgMaf() {
        return amr1kgMaf;
    }


    public Double getSas1kgMaf() {
        return sas1kgMaf;
    }


    public Double getEur1kgMaf() {
        return eur1kgMaf;
    }


    public Double getAaEspMaf() {
        return aaEspMaf;
    }


    public Double getEaEspMaf() {
        return eaEspMaf;
    }

    public Double getExacNfeMaf() {
        return exacNfeMaf;
    }

    public Double getExacSasMaf() {
        return exacSasMaf;
    }

    public Double getExacAfrMaf() {
        return exacAfrMaf;
    }

    public Double getExacEasMaf() {
        return exacEasMaf;
    }

    public Double getExacAmrMaf() {
        return exacAmrMaf;
    }

    public Double getExacOthMaf() {
        return exacOthMaf;
    }

    public Double getExacFinMaf() {
        return exacFinMaf;
    }

    public Double getExacAdjMaf() {
        return exacAdjMaf;
    }

}
