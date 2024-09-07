package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDtoShort create(@Valid @RequestBody ItemRequestDtoCreate itemRequestDtoCreate,
                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        itemRequestDtoCreate.setUserId(userId);
        log.info("Request: create item request: {}", itemRequestDtoCreate);
        return itemRequestService.create(itemRequestDtoCreate);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> findByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Request: find item requests by user id: {}", userId);
        return itemRequestService.findByUserId(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDtoShort> findAll() {
        log.info("Request: find all item requests");
        return itemRequestService.findAll();
    }

    @GetMapping("/{request-id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto findById(@PathVariable("request-id") long itemRequestId) {
        log.info("Request: find all item requests");
        return itemRequestService.findById(itemRequestId);
    }
}
