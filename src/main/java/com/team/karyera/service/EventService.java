
package com.team.karyera.service;

import com.team.karyera.dto.EventCreateRequest;
import com.team.karyera.dto.EventDto;
import com.team.karyera.dto.RegistrationRequest;
import com.team.karyera.exception.ConflictException;
import com.team.karyera.exception.ResourceNotFoundException;
import com.team.karyera.model.MeetingEvent;
import com.team.karyera.model.Registration;
import com.team.karyera.repository.MeetingEventRepository;
import com.team.karyera.repository.RegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final MeetingEventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public EventService(
            MeetingEventRepository eventRepository,
            RegistrationRepository registrationRepository
    ) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    public List<EventDto> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public EventDto getEventById(Long id) {
        MeetingEvent event = findEvent(id);
        return toDto(event);
    }

    public EventDto createEvent(EventCreateRequest request) {
        if (request.getDateTime().isBefore(LocalDateTime.now())) {
            throw new ConflictException(
                    "Tədbirin tarixi keçmişdə ola bilməz."
            );
        }

        MeetingEvent event = new MeetingEvent();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setMentorId(request.getMentorId());
        event.setDateTime(request.getDateTime());
        event.setLocation(request.getLocation());
        event.setCapacity(request.getCapacity());

        return toDto(eventRepository.save(event));
    }

    @Transactional
    public void registerForEvent(RegistrationRequest request) {
        MeetingEvent event = findEvent(request.getEventId());

        boolean alreadyRegistered =
                registrationRepository
                        .findByEventIdAndEmailIgnoreCase(
                                request.getEventId(),
                                request.getEmail()
                        )
                        .isPresent();

        if (alreadyRegistered) {
            throw new ConflictException(
                    "Bu e-poçt ünvanı ilə tədbirə artıq qeydiyyatdan keçilib."
            );
        }

        long registeredCount =
                registrationRepository.countByEventId(event.getId());

        if (registeredCount >= event.getCapacity()) {
            throw new ConflictException(
                    "Tədbirdə boş yer qalmayıb."
            );
        }

        Registration registration = new Registration();
        registration.setEventId(event.getId());
        registration.setFullName(request.getFullName());
        registration.setEmail(request.getEmail());
        registration.setRegisteredAt(LocalDateTime.now());

        registrationRepository.save(registration);
    }

    public List<Registration> getEventRegistrations(Long eventId) {
        findEvent(eventId);
        return registrationRepository.findByEventId(eventId);
    }

    private MeetingEvent findEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tədbir tapılmadı. ID: " + id
                        )
                );
    }

    private EventDto toDto(MeetingEvent event) {
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getMentorId(),
                event.getDateTime(),
                event.getLocation(),
                event.getCapacity()
        );
    }
}
