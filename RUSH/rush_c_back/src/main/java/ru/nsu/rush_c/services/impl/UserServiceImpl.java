package ru.nsu.rush_c.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.nsu.rush_c.dao.repository.UserRepository;
import ru.nsu.rush_c.error.exception.AlreadyExsistException;
import ru.nsu.rush_c.error.exception.NotFoundException;
import ru.nsu.rush_c.models.User;
import ru.nsu.rush_c.models.mapper.ModelMapper;
import ru.nsu.rush_c.payload.user.UserCreateRequest;
import ru.nsu.rush_c.payload.user.UserResponse;
import ru.nsu.rush_c.services.UserService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
//        users.forEach(user -> System.out.println("Loaded user: " + user));
        return users.stream().map(user -> modelMapper.toUserResponse(user)).collect(Collectors.toList());
    }

    @Transactional
    public UserResponse getUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found when found by id", id));
        return modelMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        User user = modelMapper.toUser(userCreateRequest);
        user.setData_create(ZonedDateTime.now());

        try{
            user = userRepository.save(user);
            return modelMapper.toUserResponse(user);
        } catch (DataIntegrityViolationException e){
            throw new AlreadyExsistException(e.getMessage());
        }
    }

    @Transactional
    public UserResponse deleteUser(int id) {
        UserResponse userResponse;
        if(userRepository.existsById(id)){
            userResponse = modelMapper.toUserResponse(userRepository.findById(id).orElse(null));
            userRepository.deleteById(id);
        }
        else{
            throw new NotFoundException("Not found", id);
        }
        return userResponse;
    }

    @Transactional
    public UserResponse updateUser(int id, UserCreateRequest userCreateRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found when found by id", id));

        user.setPassword(userCreateRequest.getPassword());
        user.setRole(userCreateRequest.getRole());
        user.setAva(userCreateRequest.getAva());

        try{
            user = userRepository.save(user);
            return modelMapper.toUserResponse(user);
        } catch (DataIntegrityViolationException e){
            throw new AlreadyExsistException(e.getMessage());
        }
    }
}
