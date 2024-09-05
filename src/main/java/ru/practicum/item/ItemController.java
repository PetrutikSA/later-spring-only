package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.item.dto.ItemCreateDto;
import ru.practicum.item.dto.ItemDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader("X-Later-User-Id") long userId) {
        return itemService.getAllItems(userId);
    }

    @PostMapping
    public ItemDto saveNewItem(@RequestHeader("X-Later-User-Id") long userId, @RequestBody ItemCreateDto itemCreateDto) {
        return itemService.saveNewItem(userId, itemCreateDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Later-User-Id") long userId, @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> get(
            @RequestHeader("X-Later-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "unread") String state,
            @RequestParam(name = "ContentType", defaultValue = "all") String contentType,
            @RequestParam(name = "sort", defaultValue = "newest") String sort,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "tags", required = false) List<String> tags
    ) {
        return null;
    }
}
