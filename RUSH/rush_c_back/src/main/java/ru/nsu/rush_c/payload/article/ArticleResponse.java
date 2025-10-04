package ru.nsu.rush_c.payload.article;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.rush_c.models.User;
import ru.nsu.rush_c.payload.practice.PracticeResponse;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ArticleResponse {
    private int id;

    private String name;

    private boolean paid;

    private int cost;

    private int user_id;

    private ZonedDateTime data_create;

    private ZonedDateTime data_update;

    private int module_id;

    private int number_in_module;

    private String content;

    private List<PracticeResponse> practices;
}
