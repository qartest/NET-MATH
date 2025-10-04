package ru.nsu.rush_c.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.rush_c.models.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
