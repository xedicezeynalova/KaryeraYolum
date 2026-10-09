
package com.team.karyera.dto;

import jakarta.validation.constraints.NotBlank;

public class RankRequest {

    @NotBlank(message = "Kateqoriya qeyd edilməlidir")
    private String category;

    private String interests;
    private String skills;

    public RankRequest() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}