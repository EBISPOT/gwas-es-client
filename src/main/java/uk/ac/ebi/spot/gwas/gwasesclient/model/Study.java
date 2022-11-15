package uk.ac.ebi.spot.gwas.gwasesclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Study {

    private String initialSampleSize;
    private boolean gxe;
    private boolean gxg;
    private long snpCount;
    private String qualifier;
    private boolean imputed;
    private boolean pooled;
    private String studyDesignComment;
    private String accessionId;
    private boolean fullPvalueSet;
    private boolean userRequested;
}
