package ru.nsu.rush_c.payload.clue;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClueResponse {
    private int id;

    private String description;

    private int practice_id;
}
