package ru.nsu.rush_c.payload.practice;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PracticeCreateRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private int article_id;

    private String code_template;

    @NotEmpty
    private String content;


}
