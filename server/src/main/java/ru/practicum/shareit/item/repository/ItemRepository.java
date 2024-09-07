package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Deprecated
public interface ItemRepository {
    Item create(Item item);

    List<Item> findItemsOfUser(long userId);

    Item findById(long itemId);

    Item update(Item item, long userId);

    void delete(long itemId, long userId);

    List<Item> searchByText(String text);
}
