package ru.practicum.item;

import org.springframework.stereotype.Repository;
import ru.practicum.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository{
    private final Map<Long, Map<Long, Item>> items = new HashMap<>();
    private long maxId;

    @Override
    public List<Item> findByUserId(long userId) {
        if (!items.containsKey(userId))
            throw new NotFoundException(String.format("У пользователя с ID = %d не найдено заметок", userId));
        return items.get(userId).values().stream().toList();
    }

    @Override
    public Item save(long userId, Item item) {
        maxId++;
        item.setId(maxId);
        if(!items.containsKey(userId)) {
            items.put(userId, new HashMap<>());
        }
        items.get(userId).put(maxId, item);
        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        if (!items.containsKey(userId))
            throw new NotFoundException(String.format("У пользователя с ID = %d не найдено заметок", userId));
        items.get(userId).remove(itemId);
    }
}
