package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import note.ItemNotesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl  implements ItemService{
    private final ItemRepository itemRepository;

    @Override
    public List<Item> getAllItems(long userId) {
        return itemRepository.findByUserId(userId);
    }

    @Override
    public Item saveNewItem(long userId, Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByUserIdAndItemId(userId, itemId);
    }
}
