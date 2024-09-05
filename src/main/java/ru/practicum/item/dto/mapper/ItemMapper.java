package ru.practicum.item.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.practicum.item.dto.ItemCreateDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemUpdateDto;
import ru.practicum.item.metadata.UrlMetadata;
import ru.practicum.item.model.Item;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {
    Item itemCreateDtoToItem(ItemCreateDto itemCreateDto);

    @Mapping(target = "normalUrl", source = "url")
    ItemDto itemToItemDto(Item item);

    @Mapping(target = "url", source = "normalUrl")
    Item fillWithMetaData(UrlMetadata urlMetadata, @MappingTarget Item item);

    @Mapping(target = "tags", expression = "java(mapTags(itemUpdateDto, item))")
    @Mapping(target = "unread", expression = "java(!itemUpdateDto.isRead())")
    Item updateItem(ItemUpdateDto itemUpdateDto, @MappingTarget Item item);

    default Set<String> mapTags(ItemUpdateDto itemUpdateDto, @MappingTarget Item item) {
        Set<String> tags = item.getTags();
        if (itemUpdateDto.isReplaceTags()) {
            tags = itemUpdateDto.getTags();
        } else {
            tags.addAll(itemUpdateDto.getTags());
        }
        return tags;
    }
}
