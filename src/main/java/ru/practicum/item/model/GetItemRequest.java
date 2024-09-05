package ru.practicum.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.item.model.enums.ContentType;
import ru.practicum.item.model.enums.ItemSort;
import ru.practicum.item.model.enums.ItemState;

import java.util.List;

@Data
@Builder
public class GetItemRequest {
    private long userId;
    private ItemState state;
    private ContentType contentType;
    private ItemSort sort;
    private int limit;
    private List<String> tags;
}
