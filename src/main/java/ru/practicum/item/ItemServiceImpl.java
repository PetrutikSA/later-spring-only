package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.item.metadata.UrlMetadata;
import ru.practicum.item.metadata.UrlMetadataRetrieverImpl;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl  implements ItemService{
    private final ItemRepository itemRepository;
    private final UrlMetadataRetrieverImpl urlMetadataRetriever;

    @Override
    public List<Item> getAllItems(long userId) {
        return itemRepository.findByUserId(userId);
    }

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

    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByUserIdAndItemId(userId, itemId);
    }
}
