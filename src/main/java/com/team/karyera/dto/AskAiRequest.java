
package com.team.karyera.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AskAiRequest {

    @NotBlank(message = "Mesaj boş ola bilməz")
    @Size(max = 2000, message = "Mesaj maksimum 2000 simvol ola bilər")
    private String message;

    public AskAiRequest() {
    }

    public AskAiRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
