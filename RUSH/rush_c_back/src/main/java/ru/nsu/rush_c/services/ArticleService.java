package ru.nsu.rush_c.services;

import ru.nsu.rush_c.payload.article.ArticleCreateRequest;
import ru.nsu.rush_c.payload.article.ArticleResponse;
import ru.nsu.rush_c.payload.user.UserCreateRequest;
import ru.nsu.rush_c.payload.user.UserResponse;

import java.util.List;

public interface ArticleService {

    List<ArticleResponse> getAllArticle();

    ArticleResponse getArticleById(int id);

    ArticleResponse createArticle(ArticleCreateRequest articleCreateRequest);

    ArticleResponse deleteArticle(int id);

    ArticleResponse updateArticle(int id, ArticleCreateRequest articleCreateRequest);
}
