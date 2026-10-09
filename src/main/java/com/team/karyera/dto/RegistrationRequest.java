
package com.team.karyera.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RegistrationRequest {

    @NotNull(message = "Tədbir ID-si tələb olunur")
    @Positive(message = "Tədbir ID-si müsbət olmalıdır")
    private Long eventId;

    @NotBlank(message = "Ad və soyad tələb olunur")
    private String fullName;

    @NotBlank(message = "E-poçt ünvanı tələb olunur")
    @Email(message = "E-poçt ünvanı düzgün deyil")
    private String email;

    public RegistrationRequest() {
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
