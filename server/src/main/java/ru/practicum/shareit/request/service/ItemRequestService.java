package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoShort create(ItemRequestDtoCreate itemRequestDtoCreate);

    List<ItemRequestDto> findByUserId(long userId);

    List<ItemRequestDtoShort> findAll();

    ItemRequestDto findById(long itemRequestId);
}
