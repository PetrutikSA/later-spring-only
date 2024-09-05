package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.metadata.UrlMetadata;
import ru.practicum.item.metadata.UrlMetadataRetrieverImpl;
import ru.practicum.item.model.Item;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
public class ItemServiceImpl  implements ItemService{
    private final ItemRepository itemRepository;
    private final UrlMetadataRetrieverImpl urlMetadataRetriever;
    private final ItemMapper itemMapper;

    @Override
    public List<ItemDto> getAllItems(long userId) {
        return itemRepository.findByUserId(userId).stream()
                .map(itemMapper::itemToItemDto)
                .toList();
    }

    @Transactional
    @Override
    public Item saveNewItem(long userId, Item item) {
        item.setUserId(userId);

        UrlMetadata urlMetadata = urlMetadataRetriever.retrieve(item.getUrl());

        Optional<Item> maybeExistingItem =
                itemRepository.findByUserIdAndResolvedUrl(userId, urlMetadata.getResolvedUrl());

        if (maybeExistingItem.isEmpty()) {
            item.setTitle(urlMetadata.getTitle());
            item.setResolvedUrl(urlMetadata.getResolvedUrl());
            item.setMimeType(urlMetadata.getMimeType());
            item.setHasImage(urlMetadata.isHasImage());
            item.setHasVideo(urlMetadata.isHasVideo());
            item.setDateResolved(urlMetadata.getDateResolved());
            item = itemRepository.save(item);
        } else {
            Item itemFromDB = maybeExistingItem.get();
            if(item.getTags() != null && !item.getTags().isEmpty()) {
                itemFromDB.getTags().addAll(item.getTags());
                item = itemRepository.save(itemFromDB);
            }
        }
        return item;
    }

    @Transactional
    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByUserIdAndId(userId, itemId);
    }
}
