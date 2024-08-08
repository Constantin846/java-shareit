package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Request: create item: {}", itemDto);
        return itemService.create(itemDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> findItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Request: find all items of user: {}", userId);
        return itemService.findItemsOfUser(userId);
    }

    @GetMapping("/{item-id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto findById(@PathVariable("item-id") long itemId) {
        log.info("Request: find item by id: {}", itemId);
        return itemService.findById(itemId);
    }

    @PatchMapping("/{item-id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable("item-id") long itemId,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        itemDto.setId(itemId);
        log.info("Request: update item: {}", itemDto);
        if (itemDto.getId() == null) {
            String message = String.format("The item's id is null: %s", itemDto);
            log.warn(message);
            throw new ValidationException(message);
        }
        return itemService.update(itemDto, userId);
    }

    @DeleteMapping("/{item-id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("item-id") long itemId,
                       @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Request: delete item by id: {}", itemId);
        itemService.delete(itemId, userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> search(@RequestParam("text") String text) {
        log.info("Request: search items by text: {}", text);
        return itemService.searchByText(text);
    }
}
