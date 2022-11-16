package uk.ac.ebi.spot.gwas.gwasesclient.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.spot.gwas.gwasesclient.connector.EsClientConnector;
import uk.ac.ebi.spot.gwas.gwasesclient.model.gwasrestapi.StudiesRestApiWrapper;
import uk.ac.ebi.spot.gwas.gwasesclient.model.gwasrestapi.Study;
import uk.ac.ebi.spot.gwas.gwasesclient.model.Sumstats;
import uk.ac.ebi.spot.gwas.gwasesclient.model.rest.ResponseMetadata;
import uk.ac.ebi.spot.gwas.gwasesclient.model.rest.RestResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SumstatsService {

    final EsClientConnector esClientConnector;
    final RestTemplate restTemplate;

    public SumstatsService(EsClientConnector esClientConnector, RestTemplate restTemplate) {
        this.esClientConnector = esClientConnector;
        this.restTemplate = restTemplate;
    }

    public RestResponse search(String hm_rsid, String hm_variant_id, String pos, String study_accession,
                               String p_value, String phenotype, Integer size, Integer from) throws IOException {
        // translate pos and chrom
        String[] posArr = {null, null};
        List<String> gcsts = new ArrayList<>();
        if (!ObjectUtils.isEmpty(pos)) {
            posArr = pos.split(":");
        }
        if (!ObjectUtils.isEmpty(study_accession)) {
            gcsts = Arrays.asList(study_accession.split(","));
        }
        if (!ObjectUtils.isEmpty(phenotype)) {
            gcsts.addAll(fetchAccessionsByEfo(phenotype));
        }
        SearchResponse<Sumstats> searchResponse = this.esClientConnector.fetchSumstats(hm_rsid, hm_variant_id, posArr[0], posArr[1], gcsts, p_value, size, from);
        RestResponse restResponse = new RestResponse();
        restResponse.setResponseMetadata(new ResponseMetadata(from, size, searchResponse.hits().total().value()));
        restResponse.setSumstats(searchResponse.hits().hits().stream().map(Hit::source).collect(Collectors.toList()));
        return restResponse;
    }

    private List<String> fetchAccessionsByEfo(String efo) {
        StudiesRestApiWrapper studiesRestApiWrapper = restTemplate.getForObject("https://www.ebi.ac.uk/gwas/rest/api/efoTraits/" + efo + "/studies", StudiesRestApiWrapper.class);
        Study[] studies = studiesRestApiWrapper.get_embedded().getStudies();
        return Arrays.asList(studies).stream().map(study -> study.getAccessionId()).collect(Collectors.toList());
    }
}
