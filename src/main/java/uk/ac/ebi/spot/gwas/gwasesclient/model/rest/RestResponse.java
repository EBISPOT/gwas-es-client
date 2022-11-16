package uk.ac.ebi.spot.gwas.gwasesclient.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.spot.gwas.gwasesclient.model.Sumstats;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"_metadata", "sumstats"})
public class RestResponse {

    @JsonProperty("_metadata")
    private ResponseMetadata responseMetadata;
    private List<Sumstats> sumstats;
}
