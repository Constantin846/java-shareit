package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, long userId);

    List<ItemDto> findItemsOfUser(long userId);

    ItemDto findById(long itemId);

    ItemDto update(ItemDto itemDto, long userId);

    void delete(long itemId, long userId);

    List<ItemDto> searchByText(String text);

    CommentDto createComment(CommentDto commentDto);
}
