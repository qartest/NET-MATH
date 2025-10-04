package ru.nsu.rush_c.payload.practice;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.rush_c.models.User;
import ru.nsu.rush_c.payload.clue.ClueResponse;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PracticeResponse {
    private int id;

    private String name;

    private int article_id;

    private String code_template;

    private String content;

    private List<ClueResponse> clues;
}
