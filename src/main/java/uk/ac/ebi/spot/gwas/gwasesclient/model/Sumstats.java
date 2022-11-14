package uk.ac.ebi.spot.gwas.gwasesclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sumstats {

    private String id;
    private String hm_beta;
    private Integer hm_chrom;
    private String hm_ci_lower;
    private String hm_ci_upper;
    private String hm_code;
    private String hm_effect_allele;
    private String hm_effect_allele_frequency;
    private String hm_odds_ratio;
    private String hm_other_allele;
    private Integer hm_pos;
    private String hm_rsid;
    private String hm_variant_id;
    private Double p_value;
    private String study_accession;
}
