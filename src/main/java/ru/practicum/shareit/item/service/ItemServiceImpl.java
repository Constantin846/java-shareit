package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final String USER_WAS_NOT_FOUND_BY_ID = "User was not found by id: %d";
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        if (userRepository.checkUserExists(userId)) {
            Item item = ItemDtoMapper.toItem(itemDto);
            item.setOwnerId(userId);
            Item createdItem = itemRepository.create(item);
            return findById(createdItem.getId());
        } else {
            String message = String.format(USER_WAS_NOT_FOUND_BY_ID, userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public List<ItemDto> findItemsOfUser(long userId) {
        if (userRepository.checkUserExists(userId)) {
            return itemRepository.findItemsOfUser(userId).stream()
                    .map(ItemDtoMapper::toItemDto)
                    .toList();
        } else {
            String message = String.format(USER_WAS_NOT_FOUND_BY_ID, userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public ItemDto findById(long itemId) {
        Item item = itemRepository.findById(itemId);
        return ItemDtoMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(ItemDto itemDto, long userId) {
        if (userRepository.checkUserExists(userId)) {
            Item item = itemRepository.update(ItemDtoMapper.toItem(itemDto), userId);
            return findById(item.getId());
        } else {
            String message = String.format(USER_WAS_NOT_FOUND_BY_ID, userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public void delete(long itemId, long userId) {
        if (userRepository.checkUserExists(userId)) {
            itemRepository.delete(itemId, userId);
        } else {
            String message = String.format(USER_WAS_NOT_FOUND_BY_ID, userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public List<ItemDto> searchByText(String text) {
        return itemRepository.searchByText(text).stream()
                .map(ItemDtoMapper::toItemDto)
                .toList();
    }
}
