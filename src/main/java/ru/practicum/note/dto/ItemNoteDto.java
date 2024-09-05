package ru.practicum.note.dto;

import lombok.Data;

@Data
public class ItemNoteDto {
    private Long id;
    private String text;
    private Long itemId;
    private String itemUrl;
    private String created;
}
