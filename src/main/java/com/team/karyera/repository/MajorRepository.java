
package com.team.karyera.repository;

import com.team.karyera.model.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorRepository extends JpaRepository<Major, Long> {

    List<Major> findByCategoryIgnoreCase(String category);

    List<Major> findByNameContainingIgnoreCase(String name);
}