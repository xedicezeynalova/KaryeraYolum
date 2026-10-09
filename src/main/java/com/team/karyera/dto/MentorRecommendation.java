
package com.team.karyera.dto;

public class MentorRecommendation {

    private Long mentorId;
    private String fullName;
    private String profession;
    private String category;
    private String bio;
    private double score;

    public MentorRecommendation() {
    }

    public MentorRecommendation(
            Long mentorId,
            String fullName,
            String profession,
            String category,
            String bio,
            double score
    ) {
        this.mentorId = mentorId;
        this.fullName = fullName;
        this.profession = profession;
        this.category = category;
        this.bio = bio;
        this.score = score;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
