
package com.team.karyera.repository;

import com.team.karyera.model.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    List<Mentor> findByCategoryIgnoreCase(String category);

    List<Mentor> findByProfessionContainingIgnoreCase(String profession);
}