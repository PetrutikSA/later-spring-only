package ru.practicum.item;

import ru.practicum.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> getAllItems(long userId);

    Item saveNewItem(long userId, Item item);

    void deleteItem(long userId, long itemId);
}
