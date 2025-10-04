package ru.nsu.rush_c.payload.article;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCreateRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private boolean paid;

    @NotEmpty
    private int cost;

    @NotEmpty
    private int user_id;

    @NotEmpty
    private int module_id;

    @NotEmpty
    private int number_in_module;

    @NotEmpty
    private String content;
}
