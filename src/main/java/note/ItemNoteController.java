package note;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemNoteController {
    private final ItemNoteService itemNoteService;

    @GetMapping(params = "url")
    public List<ItemNote> searchByUrl(@RequestHeader("X-Later-User-Id") long userId,
                                      @RequestParam(name="url") String url) {
        // возвращает список пользовательских заметок к ссылкам, соответствующим переданному URL-адресу или его части
        return itemNoteService.searchNotesByUrl(url, userId);
    }

    @GetMapping(params = "tag")
    public List<ItemNote> searchByTags(@RequestHeader("X-Later-User-Id") long userId,
                                          @RequestParam(name = "tag") String tag) {
        // возвращает список заметок пользователя к ссылкам с указанным тегом
        return itemNoteService.searchNotesByTag(userId, tag);
    }

    @GetMapping
    public List<ItemNote> listAllNotes(@RequestHeader("X-Later-User-Id") long userId,
                                          @RequestParam(name = "from", defaultValue = "0") int from,
                                          @RequestParam(name = "size", defaultValue = "10") int size) {
        // возвращает набор пользовательских заметок, соответствующий указанным параметрам пагинации
        return itemNoteService.listAllItemsWithNotes(userId, from, size);
    }

    @PostMapping
    public ItemNote add(@RequestHeader("X-Later-User-Id") Long userId, @RequestBody ItemNote itemNote) {
        // добавляет новую заметку к сохранённой ссылке
        return itemNoteService.addNewItemNote(userId, itemNote);
    }
}
