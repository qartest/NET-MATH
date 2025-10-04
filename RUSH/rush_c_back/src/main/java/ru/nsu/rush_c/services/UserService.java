package ru.nsu.rush_c.services;

import ru.nsu.rush_c.payload.user.UserCreateRequest;
import ru.nsu.rush_c.payload.user.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(int id);

    UserResponse createUser(UserCreateRequest userCreateRequest);

    UserResponse deleteUser(int id);

    UserResponse updateUser(int id, UserCreateRequest userCreateRequest);
}
