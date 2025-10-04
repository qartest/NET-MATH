package ru.nsu.rush_c.payload.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    @NotEmpty
    private String nickname;

    @NotEmpty
    private String password;

    @NotEmpty
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty
    private String role;

    private String ava;

}