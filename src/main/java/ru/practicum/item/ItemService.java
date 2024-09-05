package ru.practicum.item;

import org.springframework.stereotype.Repository;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;

import java.util.List;

@Repository
public interface ItemService {
    List<ItemDto> getAllItems(long userId);

    Item saveNewItem(long userId, Item item);

    void deleteItem(long userId, long itemId);
}
