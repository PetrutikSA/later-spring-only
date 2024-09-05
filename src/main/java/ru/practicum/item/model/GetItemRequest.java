package ru.practicum.item.model;

import lombok.Data;

import java.util.List;

@Data
public class GetItemRequest {
    private long userId;
    private ItemState state;
    private ContentType contentType;
    private String sort;
    private int limit;
    private List<String> tags;
}
