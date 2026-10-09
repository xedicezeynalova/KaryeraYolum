
package com.team.karyera.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class EventCreateRequest {

    @NotBlank(message = "Tədbirin adı boş ola bilməz")
    @Size(max = 200)
    private String title;

    @Size(max = 3000)
    private String description;

    @NotNull(message = "Mentor ID tələb olunur")
    private Long mentorId;

    @NotNull(message = "Tədbirin tarixi tələb olunur")
    private LocalDateTime dateTime;

    @NotBlank(message = "Məkan qeyd edilməlidir")
    private String location;

    @NotNull(message = "İştirakçı tutumu tələb olunur")
    @Positive(message = "Tutum müsbət olmalıdır")
    private Integer capacity;

    public EventCreateRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}