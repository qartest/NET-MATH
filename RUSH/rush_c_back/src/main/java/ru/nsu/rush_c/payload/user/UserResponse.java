package ru.nsu.rush_c.payload.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.rush_c.payload.article.ArticleResponse;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserResponse {
    private int id;

    private ZonedDateTime data_create;

    private String nickname;

    private String email;

    private String role;

    private String ava;

    private List<ArticleResponse> articles;
}