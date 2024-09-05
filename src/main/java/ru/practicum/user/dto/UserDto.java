package ru.practicum.user.dto;

import lombok.Data;
import ru.practicum.user.UserState;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private UserState state;
}
