package ru.nsu.rush_c.payload.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.rush_c.models.Article;
import ru.nsu.rush_c.payload.article.ArticleResponse;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ModuleResponse {
    private int id;

    private String name;

    private String description;

    private ZonedDateTime data_create;

    private ZonedDateTime data_update;

    private List<ArticleResponse> articles;
}
