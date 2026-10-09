
package com.team.karyera.dto;

import java.util.ArrayList;
import java.util.List;

public class RecommendResponse {

    private List<MajorRecommendation> majors = new ArrayList<>();
    private List<MentorRecommendation> mentors = new ArrayList<>();
    private String explanation;

    public RecommendResponse() {
    }

    public RecommendResponse(
            List<MajorRecommendation> majors,
            List<MentorRecommendation> mentors,
            String explanation
    ) {
        this.majors = majors;
        this.mentors = mentors;
        this.explanation = explanation;
    }

    public List<MajorRecommendation> getMajors() {
        return majors;
    }

    public void setMajors(List<MajorRecommendation> majors) {
        this.majors = majors;
    }

    public List<MentorRecommendation> getMentors() {
        return mentors;
    }

    public void setMentors(List<MentorRecommendation> mentors) {
        this.mentors = mentors;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
