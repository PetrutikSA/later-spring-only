package ru.practicum.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.practicum.item.dto.ItemCreateDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.metadata.UrlMetadata;
import ru.practicum.item.model.Item;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {
    Item itemCreateDtoToItem(ItemCreateDto itemCreateDto);

    @Mapping(target = "normalUrl", source = "url")
    ItemDto itemToItemDto(Item item);

    @Mapping(target = "url", source = "normalUrl")
    Item fillWithMetaData(UrlMetadata urlMetadata, @MappingTarget Item item);
}
