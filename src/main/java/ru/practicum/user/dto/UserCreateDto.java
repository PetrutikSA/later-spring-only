package ru.practicum.user.dto;

import lombok.Data;

@Data
public class UserCreateDto {
    private String email;
    private String name;
    private String state;
}
