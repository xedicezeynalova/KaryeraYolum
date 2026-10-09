
package com.team.karyera.controller;

import com.team.karyera.dto.EventCreateRequest;
import com.team.karyera.dto.EventDto;
import com.team.karyera.dto.MessageResponse;
import com.team.karyera.dto.RegistrationRequest;
import com.team.karyera.model.Registration;
import com.team.karyera.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventDto> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventDto getEvent(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @Valid @RequestBody EventCreateRequest request
    ) {
        EventDto event = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(
            @Valid @RequestBody RegistrationRequest request
    ) {
        eventService.registerForEvent(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse(
                        "Tədbirə qeydiyyat uğurla tamamlandı.",
                        true
                ));
    }

    @GetMapping("/{id}/registrations")
    public List<Registration> getRegistrations(
            @PathVariable Long id
    ) {
        return eventService.getEventRegistrations(id);
    }
}