package ru.practicum.item.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ItemUpdateDto {
    private long itemId;
    private boolean read;
    private Set<String> tags;
    private boolean replaceTags;
}
