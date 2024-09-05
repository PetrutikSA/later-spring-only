package ru.practicum.item;

import ru.practicum.item.dto.ItemCreateDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemUpdateDto;
import ru.practicum.item.model.GetItemRequest;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAllItems(long userId);

    ItemDto saveNewItem(long userId, ItemCreateDto itemCreateDto);

    void deleteItem(long userId, long itemId);

    List<ItemDto> getItems(GetItemRequest req);

    ItemDto updateItem(long userId, ItemUpdateDto itemUpdateDto);
}
