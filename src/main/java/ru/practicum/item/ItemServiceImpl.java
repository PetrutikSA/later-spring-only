package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.InsufficientPermissionException;
import ru.practicum.item.dto.ItemCreateDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.mapper.ItemMapper;
import ru.practicum.item.metadata.UrlMetadata;
import ru.practicum.item.metadata.UrlMetadataRetrieverImpl;
import ru.practicum.item.model.GetItemRequest;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;
import ru.practicum.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
public class ItemServiceImpl  implements ItemService{
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
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
    public ItemDto saveNewItem(long userId, ItemCreateDto itemCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new InsufficientPermissionException("You do not have permission to perform this operation"));

        Item item = itemMapper.itemCreateDtoToItem(itemCreateDto);
        item.setUserId(userId);

        UrlMetadata urlMetadata = urlMetadataRetriever.retrieve(item.getUrl());
        Optional<Item> maybeExistingItem =
                itemRepository.findByUserIdAndResolvedUrl(userId, urlMetadata.getResolvedUrl());

        if (maybeExistingItem.isEmpty()) {
            item = itemMapper.fillWithMetaData(urlMetadata, item);
            item = itemRepository.save(item);
        } else {
            Item itemFromDB = maybeExistingItem.get();
            if(item.getTags() != null && !item.getTags().isEmpty()) {
                itemFromDB.getTags().addAll(item.getTags());
                item = itemRepository.save(itemFromDB);
            }
        }
        return itemMapper.itemToItemDto(item);
    }

    @Transactional
    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByUserIdAndId(userId, itemId);
    }

    @Override
    public List<ItemDto> getItems(GetItemRequest req) {
        return null;
    }
}
