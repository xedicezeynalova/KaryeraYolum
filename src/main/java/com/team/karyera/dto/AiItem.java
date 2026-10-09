
package com.team.karyera.dto;

public class AiItem {

    private String name;
    private String category;

    public AiItem() {
    }

    public AiItem(String name, String category) {
        this.name = name;
        this.category = category;
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
}
