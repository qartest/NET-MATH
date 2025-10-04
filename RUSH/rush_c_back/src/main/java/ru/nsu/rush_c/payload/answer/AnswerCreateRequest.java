package ru.nsu.rush_c.payload.answer;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerCreateRequest {

    @NotEmpty
    private String content;

    @NotEmpty
    private int count_likes;

    @NotEmpty
    private int user_id;

    @NotEmpty
    private int practice_id;



}
