package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUserId(long userId);

    void deleteByUserIdAndItemId(long userId, long itemId);

    Optional<Item> findByUserIdAndResolvedUrl(long userId, String resolvedUrl);
}
