package uk.ac.ebi.spot.gwas.gwasesclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.spot.gwas.gwasesclient.model.Sumstats;
import uk.ac.ebi.spot.gwas.gwasesclient.model.rest.RestResponse;
import uk.ac.ebi.spot.gwas.gwasesclient.service.SumstatsService;

import java.io.IOException;
import java.util.List;

@RestController()
@RequestMapping("/search")
public class SearchController {

    final
    SumstatsService sumstatsService;

    public SearchController(SumstatsService sumstatsService) {
        this.sumstatsService = sumstatsService;
    }

    @GetMapping
    RestResponse findSumstats(@RequestParam(required = false) String hm_rsid, @RequestParam(required = false) String hm_variant_id,
                              @RequestParam(required = false) String pos, @RequestParam(required = false) String study_accession,
                              @RequestParam(required = false) String p_value_threshold, @RequestParam(required = false) String phenotype,
                              @RequestParam(required = false, defaultValue = "20") Integer size, @RequestParam(required = false, defaultValue = "0") Integer from) throws IOException {
        return sumstatsService.search(hm_rsid, hm_variant_id, pos, study_accession, p_value_threshold, phenotype, size, from);
    }
}
