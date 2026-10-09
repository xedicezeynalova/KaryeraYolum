
package com.team.karyera.dto;

public class MajorRecommendation {

    private Long majorId;
    private String name;
    private String category;
    private String description;
    private String careers;
    private double score;

    public MajorRecommendation() {
    }

    public MajorRecommendation(
            Long majorId,
            String name,
            String category,
            String description,
            String careers,
            double score
    ) {
        this.majorId = majorId;
        this.name = name;
        this.category = category;
        this.description = description;
        this.careers = careers;
        this.score = score;
    }

    public Long getMajorId() {
        return majorId;
    }

    public void setMajorId(Long majorId) {
        this.majorId = majorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCareers() {
        return careers;
    }

    public void setCareers(String careers) {
        this.careers = careers;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
