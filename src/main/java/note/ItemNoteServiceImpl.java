package note;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemNoteServiceImpl implements ItemNoteService{
    private final ItemNotesRepository itemNotesRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemNote addNewItemNote(long userId, ItemNote itemNote) {
        Item item = itemRepository.findById(itemNote.getItem().getId())
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return itemNotesRepository.save(itemNote);
    }

    @Override
    public List<ItemNote> searchNotesByUrl(String url, Long userId) {
        return itemNotesRepository.findAllByItemUserIdAndItemUrlContainingIgnoreCase(userId, url);
    }

    @Override
    public List<ItemNote> searchNotesByTag(long userId, String tag) {
        return itemNotesRepository.findItemNotesByUserIdAndTag(userId, tag);
    }

    @Override
    public List<ItemNote> listAllItemsWithNotes(long userId, int from, int size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        return itemNotesRepository.findAllByItemUserId(userId, page).stream().toList();
    }
}
