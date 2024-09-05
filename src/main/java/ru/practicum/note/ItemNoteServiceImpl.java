package ru.practicum.note;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.item.model.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.note.dto.ItemNoteCreateDto;
import ru.practicum.note.dto.ItemNoteDto;
import ru.practicum.note.dto.mapper.ItemNoteMapper;
import ru.practicum.note.model.ItemNote;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemNoteServiceImpl implements ItemNoteService{
    private final ItemNotesRepository itemNotesRepository;
    private final ItemRepository itemRepository;
    private final ItemNoteMapper itemNoteMapper;

    @Override
    public ItemNoteDto addNewItemNote(long userId, ItemNoteCreateDto itemNoteCreateDto) {
        Item item = itemRepository.findById(itemNoteCreateDto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));
        ItemNote itemNote = itemNoteMapper.itemNoteCreateDtoToItemNote(itemNoteCreateDto, item);
        itemNote.setCreated(Instant.now());
        itemNote = itemNotesRepository.save(itemNote);
        return itemNoteMapper.itemNoteToItemNoteDto(itemNote);
    }

    @Override
    public List<ItemNoteDto> searchNotesByUrl(String url, Long userId) {
        return itemNotesRepository.findAllByItemUserIdAndItemUrlContainingIgnoreCase(userId, url).stream()
                .map(itemNoteMapper::itemNoteToItemNoteDto)
                .toList();
    }

    @Override
    public List<ItemNoteDto> searchNotesByTag(long userId, String tag) {
        return itemNotesRepository.findItemNotesByUserIdAndTag(userId, tag).stream()
                .map(itemNoteMapper::itemNoteToItemNoteDto)
                .toList();
    }

    @Override
    public List<ItemNoteDto> listAllItemsWithNotes(long userId, int from, int size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        return itemNotesRepository.findAllByItemUserId(userId, page).stream()
                .map(itemNoteMapper::itemNoteToItemNoteDto)
                .toList();
    }
}
