package ru.practicum.note;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.note.dto.ItemNoteCreateDto;
import ru.practicum.note.dto.ItemNoteDto;

import java.util.List;

@Transactional(readOnly = true)
interface ItemNoteService {

    @Transactional
    ItemNoteDto addNewItemNote(long userId, ItemNoteCreateDto itemNoteCreateDto);

    List<ItemNoteDto> searchNotesByUrl(String url, Long userId);

    List<ItemNoteDto> searchNotesByTag(long userId, String tag);

    List<ItemNoteDto> listAllItemsWithNotes(long userId, int from, int size);
}
