
package com.team.karyera.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AskRequest {

    @NotBlank(message = "Sual boş ola bilməz")
    @Size(max = 2000, message = "Sual maksimum 2000 simvol ola bilər")
    private String question;

    public AskRequest() {
    }

    public AskRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
