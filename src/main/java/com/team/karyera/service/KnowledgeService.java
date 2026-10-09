
package com.team.karyera.service;

import com.team.karyera.dto.AskRequest;
import com.team.karyera.dto.AskResponse;
import com.team.karyera.exception.ResourceNotFoundException;
import com.team.karyera.model.Article;
import com.team.karyera.model.Major;
import com.team.karyera.repository.ArticleRepository;
import com.team.karyera.repository.MajorRepository;
import com.team.karyera.repository.MentorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeService {

    private final ArticleRepository articleRepository;
    private final MajorRepository majorRepository;
    private final MentorRepository mentorRepository;
    private final AiClient aiClient;

    public KnowledgeService(
            ArticleRepository articleRepository,
            MajorRepository majorRepository,
            MentorRepository mentorRepository,
            AiClient aiClient
    ) {
        this.articleRepository = articleRepository;
        this.majorRepository = majorRepository;
        this.mentorRepository = mentorRepository;
        this.aiClient = aiClient;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public List<Article> getArticlesByCategory(String category) {
        return articleRepository.findByCategoryIgnoreCase(category);
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Məqalə tapılmadı. ID: " + id
                        )
                );
    }

    public List<Major> getAllMajors() {
        return majorRepository.findAll();
    }

    public List<Major> searchMajors(String name) {
        return majorRepository.findByNameContainingIgnoreCase(name);
    }

    public AskResponse askQuestion(AskRequest request) {
        String answer = aiClient.ask(request.getQuestion());

        return new AskResponse(answer, "AI");
    }

    public int getArticleCount() {
        return (int) articleRepository.count();
    }

    public int getMajorCount() {
        return (int) majorRepository.count();
    }

    public int getMentorCount() {
        return (int) mentorRepository.count();
    }
}
