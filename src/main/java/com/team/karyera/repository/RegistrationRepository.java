
package com.team.karyera.repository;

import com.team.karyera.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository
        extends JpaRepository<Registration, Long> {

    List<Registration> findByEventId(Long eventId);

    Optional<Registration> findByEventIdAndEmailIgnoreCase(
            Long eventId,
            String email
    );

    long countByEventId(Long eventId);
}