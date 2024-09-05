package ru.practicum.note.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import ru.practicum.item.model.Item;
import ru.practicum.note.dto.ItemNoteCreateDto;
import ru.practicum.note.dto.ItemNoteDto;
import ru.practicum.note.model.ItemNote;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemNoteMapper {
    @Mapping(source = "item", target = "item")
    @Mapping(source = "itemNoteCreateDto.text", target = "text")
    ItemNote itemNoteCreateDtoToItemNote(ItemNoteCreateDto itemNoteCreateDto, Item item);

    @Mapping(source = "itemNote.item.url", target = "itemUrl")
    @Mapping(source = "itemNote.item.id", target = "itemId")
    ItemNoteDto itemNoteToItemNoteDto(ItemNote itemNote);
}
