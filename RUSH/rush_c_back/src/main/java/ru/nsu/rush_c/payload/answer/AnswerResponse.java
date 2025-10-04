package ru.nsu.rush_c.payload.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.rush_c.payload.practice.PracticeResponse;
import ru.nsu.rush_c.payload.user.UserResponse;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AnswerResponse {

    private int id;

    private String content;

    private int count_likes;

    private UserResponse userResponse;

    private PracticeResponse practiceResponse;

}
