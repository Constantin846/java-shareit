package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoCreate;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDtoCreate itemDtoCreate, long userId);

    List<ItemDto> findItemsOfUser(long userId);

    ItemDto findById(long itemId);

    ItemBookingCommentDto findItemBookingCommentsById(long itemId);

    ItemDto update(ItemDto itemDto, long userId);

    void delete(long itemId, long userId);

    List<ItemDto> searchByText(String text);

    CommentDto createComment(CommentDto commentDto, long userId);
}
