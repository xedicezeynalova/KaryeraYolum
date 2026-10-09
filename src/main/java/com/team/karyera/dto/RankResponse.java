
package com.team.karyera.dto;

import java.util.ArrayList;
import java.util.List;

public class RankResponse {

    private String category;
    private List<MajorRecommendation> recommendations = new ArrayList<>();

    public RankResponse() {
    }

    public RankResponse(
            String category,
            List<MajorRecommendation> recommendations
    ) {
        this.category = category;
        this.recommendations = recommendations;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<MajorRecommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(
            List<MajorRecommendation> recommendations
    ) {
        this.recommendations = recommendations;
    }
}
