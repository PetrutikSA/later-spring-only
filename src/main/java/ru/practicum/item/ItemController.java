package ru.practicum.item;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @GetMapping
    public List<Item> getAllItems(@RequestHeader("X-Later-User-Id") long userId) {
        return null;
    }

    @PostMapping
    public Item saveNewItem(@RequestHeader("X-Later-User-Id") long userId, @RequestBody Item item) {
        return null;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable long itemId) {
    }
}
