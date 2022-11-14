package uk.ac.ebi.spot.gwas.gwasesclient.service;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import uk.ac.ebi.spot.gwas.gwasesclient.connector.EsClientConnector;
import uk.ac.ebi.spot.gwas.gwasesclient.model.Sumstats;

import java.io.IOException;
import java.util.List;

@Service
public class SumstatsService {

    final
    EsClientConnector esClientConnector;

    public SumstatsService(EsClientConnector esClientConnector) {
        this.esClientConnector = esClientConnector;
    }

    public List<Sumstats> search(String hm_rsid, String hm_variant_id, String pos, String study_accession, String p_value, String phenotype) throws IOException {
        // translate pos and chrom
        String[] posArr = {null, null};
        if (!ObjectUtils.isEmpty(pos)) {
            posArr = pos.split(":");
        }
        return this.esClientConnector.fetchSumstats(hm_rsid, hm_variant_id, posArr[0], posArr[1], study_accession, p_value, phenotype);
    }
}
