package uk.ac.ebi.spot.gwas.gwasesclient.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"size", "from", "total_record_count"})
public class ResponseMetadata {

    private int from;
    private int size;
    @JsonProperty("total_record_count")
    private Long totalRecordCount;
}
