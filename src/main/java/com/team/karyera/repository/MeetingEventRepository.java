
package com.team.karyera.repository;

import com.team.karyera.model.MeetingEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingEventRepository
        extends JpaRepository<MeetingEvent, Long> {

    List<MeetingEvent> findByMentorId(Long mentorId);
}




