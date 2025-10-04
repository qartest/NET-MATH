package ru.nsu.rush_c.payload.clue;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClueCreateRequest {
    @NotEmpty
    private String description;

    @NotEmpty
    private int practice_id;
}
