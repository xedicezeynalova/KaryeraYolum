
package com.team.karyera.controller;

import com.team.karyera.model.Major;
import com.team.karyera.model.Mentor;
import com.team.karyera.repository.MajorRepository;
import com.team.karyera.repository.MentorRepository;
import com.team.karyera.service.KnowledgeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final KnowledgeService knowledgeService;
    private final MajorRepository majorRepository;
    private final MentorRepository mentorRepository;

    public CatalogController(
            KnowledgeService knowledgeService,
            MajorRepository majorRepository,
            MentorRepository mentorRepository
    ) {
        this.knowledgeService = knowledgeService;
        this.majorRepository = majorRepository;
        this.mentorRepository = mentorRepository;
    }

    @GetMapping("/majors")
    public List<Major> getMajors() {
        return knowledgeService.getAllMajors();
    }

    @GetMapping("/majors/search")
    public List<Major> searchMajors(@RequestParam String name) {
        return knowledgeService.searchMajors(name);
    }

    @GetMapping("/majors/category")
    public List<Major> getMajorsByCategory(
            @RequestParam String category
    ) {
        return majorRepository.findByCategoryIgnoreCase(category);
    }

    @GetMapping("/mentors")
    public List<Mentor> getMentors() {
        return mentorRepository.findAll();
    }

    @GetMapping("/mentors/category")
    public List<Mentor> getMentorsByCategory(
            @RequestParam String category
    ) {
        return mentorRepository.findByCategoryIgnoreCase(category);
    }
}
