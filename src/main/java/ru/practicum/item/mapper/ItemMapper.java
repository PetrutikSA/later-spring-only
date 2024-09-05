package ru.practicum.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.item.dto.ItemCreateDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {
    Item itemCreateDtoToItem(ItemCreateDto itemCreateDto);

    ItemDto itemToItemDto(Item item);
}
