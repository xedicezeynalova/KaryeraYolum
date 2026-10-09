
package com.team.karyera.controller;

import com.team.karyera.dto.ProfileRequest;
import com.team.karyera.dto.RankRequest;
import com.team.karyera.dto.RankResponse;
import com.team.karyera.dto.RecommendResponse;
import com.team.karyera.service.RecommendService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    @PostMapping
    public RecommendResponse recommend(
            @Valid @RequestBody ProfileRequest request
    ) {
        return recommendService.recommend(request);
    }

    @PostMapping("/rank")
    public RankResponse rankMajors(
            @Valid @RequestBody RankRequest request
    ) {
        return recommendService.rankMajors(request);
    }
}
