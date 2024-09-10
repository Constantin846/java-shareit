package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static ru.practicum.shareit.util.StringManager.X_SHARER_USER_ID;

/**
 * TODO Sprint add-item-requests.
 */

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestDtoCreate itemRequestDtoCreate,
                                         @RequestHeader(X_SHARER_USER_ID) long userId) {
        itemRequestDtoCreate.setUserId(userId);
        log.info("Request: create item request: {}", itemRequestDtoCreate);
        return itemRequestClient.post(userId, itemRequestDtoCreate);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findByUserId(@RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Request: find item requests by user id: {}", userId);
        return itemRequestClient.findByUserId(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAll() {
        log.info("Request: find all item requests");
        return itemRequestClient.findAll();
    }

    @GetMapping("/{request-id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findById(@PathVariable("request-id") long itemRequestId) {
        log.info("Request: find all item requests");
        return itemRequestClient.findById(itemRequestId);
    }
}
