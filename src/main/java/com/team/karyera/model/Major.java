
package com.team.karyera.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "majors")
public class Major {

    @Id
    private Long id;

    private String name;
    private String category;
    private String requiredSubjects;
    private String description;
    private String careers;
    private String universities;

    public Major() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRequiredSubjects() {
        return requiredSubjects;
    }

    public void setRequiredSubjects(String requiredSubjects) {
        this.requiredSubjects = requiredSubjects;
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

    public String getUniversities() {
        return universities;
    }

    public void setUniversities(String universities) {
        this.universities = universities;
    }
}
