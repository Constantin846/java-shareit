package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import static ru.practicum.shareit.util.StringManager.X_SHARER_USER_ID;

/**
 * Controller for items
 */

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;
    private static final String ITEM_ID = "item-id";
    private static final String PATH_ITEM_ID = "/{item-id}";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Validated(Create.class) @RequestBody ItemDtoCreate itemDtoCreate,
                                         @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Request: create item: {}", itemDtoCreate);
        return itemClient.post(userId, itemDtoCreate);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findItemsOfUser(@RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Request: find all items of user: {}", userId);
        return itemClient.findItemsOfUser(userId);
    }

    @GetMapping(PATH_ITEM_ID)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findById(@PathVariable(ITEM_ID) long itemId) {
        log.info("Request: find item by id: {}", itemId);
        return itemClient.findById(itemId);
    }

    @PatchMapping(PATH_ITEM_ID)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@Validated(Update.class) @RequestBody ItemDto itemDto,
                          @PathVariable(ITEM_ID) Long itemId,
                          @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Request: update item={} by id={} ", itemDto, itemId);
        if (itemId == null) {
            String message = String.format("The item's id is null: %s", itemDto);
            log.warn(message);
            throw new ValidationException(message);
        }
        return itemClient.patch(itemId, userId, itemDto);
    }

    @DeleteMapping(PATH_ITEM_ID)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(ITEM_ID) long itemId,
                       @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Request: delete item by id: {}", itemId);
        itemClient.delete(itemId, userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> search(@RequestParam("text") String text) {
        log.info("Request: search items by text: {}", text);
        if (text.isBlank()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return itemClient.searchByText(text);
    }

    @PostMapping(PATH_ITEM_ID + "/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                                               @PathVariable(ITEM_ID) long itemId,
                                               @RequestHeader(X_SHARER_USER_ID) long userId) {
        commentDto.setItemId(itemId);
        log.info("Request: create comment={} by user={}", commentDto, userId);
        return itemClient.postComment(itemId, userId, commentDto);
    }
}
