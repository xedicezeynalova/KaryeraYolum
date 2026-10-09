
package com.team.karyera.repository;

import com.team.karyera.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByCategoryIgnoreCase(String category);

    List<Article> findByTitleContainingIgnoreCase(String title);
}