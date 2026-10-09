
package com.team.karyera.controller;

import com.team.karyera.dto.AskRequest;
import com.team.karyera.dto.AskResponse;
import com.team.karyera.model.Article;
import com.team.karyera.service.KnowledgeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @GetMapping("/articles")
    public List<Article> getArticles() {
        return knowledgeService.getAllArticles();
    }

    @GetMapping("/articles/{id}")
    public Article getArticle(@PathVariable Long id) {
        return knowledgeService.getArticleById(id);
    }

    @GetMapping("/articles/category")
    public List<Article> getArticlesByCategory(
            @RequestParam String category
    ) {
        return knowledgeService.getArticlesByCategory(category);
    }

    @PostMapping("/ask")
    public AskResponse askQuestion(
            @Valid @RequestBody AskRequest request
    ) {
        return knowledgeService.askQuestion(request);
    }
}
