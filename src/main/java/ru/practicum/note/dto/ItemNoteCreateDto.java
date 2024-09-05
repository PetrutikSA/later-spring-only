package ru.practicum.note.dto;

import lombok.Data;

@Data
public class ItemNoteCreateDto {
    private String text;
    private Long itemId;
    private String itemUrl;
}
