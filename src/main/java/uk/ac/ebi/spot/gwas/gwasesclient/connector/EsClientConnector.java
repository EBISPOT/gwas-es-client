package uk.ac.ebi.spot.gwas.gwasesclient.connector;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import uk.ac.ebi.spot.gwas.gwasesclient.model.Sumstats;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EsClientConnector {

    private final ElasticsearchClient elasticsearchClient;

    private final String index = ".ds-gwas-mixed-dev-2022.10.27-000001";

    public EsClientConnector(ElasticsearchClient elasticsearchClient) throws IOException {
        this.elasticsearchClient = elasticsearchClient;
        // System.out.println(fetchSumstatsById("Q1BRGoQBGEkvbfUzm_SJ"));
    }

    public List<Sumstats> fetchSumstats(String hm_rsid, String hm_variant_id, String hm_chrom, String hm_pos,
                                        List<String> study_accession, String p_value, Integer size, Integer from) throws IOException {
        List<Query> queries = prepareQueryList(hm_rsid, hm_variant_id, hm_chrom, hm_pos, study_accession, p_value);
        queries.forEach(query -> {
            System.out.println(query.toString());
        });
        SearchResponse<Sumstats> sumstatsSearchResponse = elasticsearchClient.search(req ->
                        req.index(index)
                                .size(size)
                                .from(from)
                                .query(query ->
                                        query.bool(bool ->
                                                bool.must(queries)))
                , Sumstats.class);
        return sumstatsSearchResponse.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }

    private List<Query> prepareQueryList(String hm_rsid, String hm_variant_id, String hm_chrom, String hm_pos, List<String> study_accession, String p_value) {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("hm_rsid", hm_rsid);
        conditionMap.put("hm_variant_id", hm_variant_id);
        conditionMap.put("hm_chrom", hm_chrom);
        conditionMap.put("hm_pos", hm_pos);
        conditionMap.put("study_accession", study_accession);
        conditionMap.put("p_value", p_value);
        return conditionMap.entrySet()
                .stream()
                .filter(entry->!ObjectUtils.isEmpty(entry.getValue()))
                .map(entry->termQuery(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private Query termQuery(String field, Object value) {
        QueryVariant queryVariant;
        if (field.equals("p_value")) {
            queryVariant = new RangeQuery.Builder()
                    .field("p_value")
                    .lte(JsonData.of(Double.parseDouble(value.toString())))
                    .build();
        }
        else if (field.equals("study_accession")) {
            List<String> gcsts = (List<String>) value;
            List<FieldValue> termValues = gcsts.stream().map(FieldValue::of).collect(Collectors.toList());
            queryVariant = new TermsQuery.Builder().field(field).terms(tf -> tf.value(termValues)).build();
        }
        else if (field.equals("hm_chrom")) {
            queryVariant = new TermQuery.Builder().field(field).value(Integer.parseInt(value.toString())).build();
        }
        else if (field.equals("hm_pos")) {
            String[] posArr = value.toString().split("-");
            queryVariant = new RangeQuery.Builder()
                    .field("hm_pos")
                    .gte(JsonData.of(Integer.parseInt(posArr[0])))
                    .lte(JsonData.of(Integer.parseInt(posArr[1])))
                    .build();
        }
        else {
            queryVariant = new TermQuery.Builder().caseInsensitive(true).field(field).value(value.toString()).build();
        }

        return new Query(queryVariant);
    }

    public Sumstats fetchSumstatsById(String id) throws IOException {
        GetResponse<Sumstats> response = elasticsearchClient.get(req-> req.index(index).id(id),Sumstats.class);
        return response.source();
    }
}
