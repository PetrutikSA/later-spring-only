package ru.practicum.item;

import org.springframework.stereotype.Repository;
import ru.practicum.item.dto.ItemCreateDto;
import ru.practicum.item.dto.ItemDto;

import java.util.List;

@Repository
public interface ItemService {
    List<ItemDto> getAllItems(long userId);

    ItemDto saveNewItem(long userId, ItemCreateDto itemCreateDto);

    void deleteItem(long userId, long itemId);
}
