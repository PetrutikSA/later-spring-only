package ru.practicum.item.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ItemCreateDto {
    private String url;
    private Set<String> tags;
}
