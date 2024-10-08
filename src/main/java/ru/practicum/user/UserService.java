package ru.practicum.user;

import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto saveUser(UserCreateDto userCreateDto);
}
