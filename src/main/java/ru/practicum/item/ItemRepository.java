package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUserId(long userId);

    void deleteByUserIdAndId(long userId, long itemId);

    Optional<Item> findByUserIdAndResolvedUrl(long userId, String resolvedUrl);
}
