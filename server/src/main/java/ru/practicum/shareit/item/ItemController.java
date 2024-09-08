package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * Controller for items
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private static final String ITEM_ID = "item-id";
    private static final String PATH_ITEM_ID = "/{item-id}";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestBody ItemDtoCreate itemDtoCreate,
                          @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Request: create item: {}", itemDtoCreate);
        return itemService.create(itemDtoCreate, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> findItemsOfUser(@RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Request: find all items of user: {}", userId);
        return itemService.findItemsOfUser(userId);
    }

    @GetMapping(PATH_ITEM_ID)
    @ResponseStatus(HttpStatus.OK)
    public ItemBookingCommentDto findById(@PathVariable(ITEM_ID) long itemId) {
        log.info("Request: find item by id: {}", itemId);
        return itemService.findItemBookingCommentsById(itemId);
    }

    @PatchMapping(PATH_ITEM_ID)
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable(ITEM_ID) long itemId,
                          @RequestHeader(X_SHARER_USER_ID) long userId) {
        itemDto.setId(itemId);
        log.info("Request: update item: {}", itemDto);
        if (itemDto.getId() == null) { //todo
            String message = String.format("The item's id is null: %s", itemDto);
            log.warn(message);
            throw new ValidationException(message);
        }
        return itemService.update(itemDto, userId);
    }

    @DeleteMapping(PATH_ITEM_ID)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(ITEM_ID) long itemId,
                       @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Request: delete item by id: {}", itemId);
        itemService.delete(itemId, userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> search(@RequestParam("text") String text) {
        log.info("Request: search items by text: {}", text);
        if (text.isBlank()) {
            return List.of();
        }
        return itemService.searchByText(text);
    }

    @PostMapping(PATH_ITEM_ID + "/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody CommentDto commentDto,
                                    @PathVariable(ITEM_ID) long itemId,
                                    @RequestHeader(X_SHARER_USER_ID) long userId) {
        commentDto.setItemId(itemId);
        log.info("Request: create comment={} by user={}", commentDto, userId);
        return itemService.createComment(commentDto, userId);
    }
}
