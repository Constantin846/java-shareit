package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;
import ru.practicum.shareit.request.repository.ItemRequestJpaRepository;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestDtoMapper itemRequestDtoMapper;
    private final ItemRequestJpaRepository itemRequestRepository;
    private final UserJpaRepository userRepository;

    @Override
    public ItemRequestDtoShort create(ItemRequestDtoCreate itemRequestDtoCreate) {
        ItemRequest itemRequest = itemRequestDtoMapper.toItemRequest(itemRequestDtoCreate);
        itemRequest.setCreated(LocalDateTime.now());

        if (!userRepository.existsById(itemRequest.getUserId())) {
            String message = String.format("User was not found by id: %d", itemRequest.getUserId());
            log.warn(message);
            throw new NotFoundException(message);
        }
        itemRequest = itemRequestRepository.save(itemRequest);
        return findById(itemRequest.getId());
    }

    @Override
    public List<ItemRequestDto> findByUserId(long userId) {
        Sort sort = Sort.by("created").ascending();
        List<ItemRequest> itemRequests = itemRequestRepository.findByUserId(userId, sort);
        return itemRequestDtoMapper.toItemRequestDto(itemRequests);
    }

    @Override
    public List<ItemRequestDtoShort> findAll() {
        Sort sort = Sort.by("created").ascending();
        List<ItemRequest> itemRequests = itemRequestRepository.findAll(sort);
        return itemRequestDtoMapper.toItemRequestDtoShort(itemRequests);
    }

    public ItemRequestDto findById(long itemRequestId) {
        return itemRequestDtoMapper.toItemRequestDto(
                itemRequestRepository.findById(itemRequestId).orElseThrow(() -> {
            String message = String.format("Item request was not found by id: %d", itemRequestId);
            log.warn(message);
            return new NotFoundException(message);
        }));
    }
}
