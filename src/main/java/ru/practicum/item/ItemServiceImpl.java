package ru.practicum.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
import ru.practicum.item.model.QItem;
import ru.practicum.item.model.enums.ContentType;
import ru.practicum.item.model.enums.ItemSort;
import ru.practicum.item.model.enums.ItemState;
import ru.practicum.user.model.User;
import ru.practicum.user.UserRepository;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
        QItem item = QItem.item;
        List<BooleanExpression> conditions = new ArrayList<>();
        conditions.add(item.userId.eq(req.getUserId()));
        ItemState state = req.getState();
        if(!state.equals(ItemState.ALL)) {
            conditions.add(makeStateCondition(state));
        }
        ContentType contentType = req.getContentType();
        if(!contentType.equals(ContentType.ALL)) {
            conditions.add(makeContentTypeCondition(contentType));
        }
        if (req.getTags() != null && !req.getTags().isEmpty()) {
            conditions.add(item.tags.any().in(req.getTags()));
        }

        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        Sort sort = makeOrderByClause(req.getSort());
        PageRequest pageRequest = PageRequest.of(0, req.getLimit(), sort);

        Iterable<Item> items = itemRepository.findAll(finalCondition, pageRequest);

        return StreamSupport.stream(items.spliterator(), false)
                .map(itemMapper::itemToItemDto)
                .toList();
    }

    private BooleanExpression makeStateCondition(ItemState state) {
        if (state == ItemState.UNREAD) {
            return QItem.item.unread.isTrue();
        } else {
            return QItem.item.unread.isFalse();
        }
    }

    private BooleanExpression makeContentTypeCondition(ContentType contentType) {
        if (contentType == ContentType.ARTICLE) {
            return QItem.item.mimeType.eq("text");
        } else if (contentType == ContentType.IMAGE) {
            return QItem.item.mimeType.eq("image");
        } else {
            return QItem.item.mimeType.eq("video");
        }
    }

    private Sort makeOrderByClause(ItemSort sort) {
        return switch (sort) {
            case TITLE -> Sort.by("title").ascending();
            case SITE -> Sort.by("resolvedUrl").ascending();
            case OLDEST -> Sort.by("dateResolved").ascending();
            default -> Sort.by("dateResolved").descending();
        };
    }
}
