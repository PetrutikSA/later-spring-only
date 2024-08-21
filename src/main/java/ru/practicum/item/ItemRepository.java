package ru.practicum.item;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository {
    List<Item> findByUserId(long userId);

    Item save(long userId, Item item);

    void deleteByUserIdAndItemId(long userId, long itemId);
}
