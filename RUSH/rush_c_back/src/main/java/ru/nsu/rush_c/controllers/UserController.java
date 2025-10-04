package ru.nsu.rush_c.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.rush_c.payload.user.UserCreateRequest;
import ru.nsu.rush_c.payload.user.UserResponse;
import ru.nsu.rush_c.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> gerAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") int id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest userCreateRequest){
        return new ResponseEntity<>(userService.createUser(userCreateRequest), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable("id") int id){
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") int id, @RequestBody UserCreateRequest userCreateRequest){
        return new ResponseEntity<>(userService.updateUser(id, userCreateRequest), HttpStatus.OK);
    }
}
