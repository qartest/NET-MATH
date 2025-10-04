package ru.nsu.rush_c.payload.module;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleCreateRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;
}
