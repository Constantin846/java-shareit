package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotAccessException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemRepositoryMemo implements ItemRepository {
    private final Map<Long, Item> items;

    @Override
    public Item create(Item item) {
        item.setId(generateId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> findItemsOfUser(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId() == userId)
                .toList();
    }

    @Override
    public Item findById(long itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            String message = String.format("An item was not found by id: %d", itemId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public Item update(Item item, long userId) {
        if (item.getId() == null) {
            String message = String.format("The item's id is null: %s", item);
            log.warn(message);
            throw new ValidationException(message);
        }

        if (items.containsKey(item.getId())) {
            Item oldItem = items.get(item.getId());

            if (oldItem.getOwnerId() == userId) {
                if (item.getName() != null) {
                    oldItem.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    oldItem.setDescription(item.getDescription());
                }
                oldItem.setAvailable(item.isAvailable());
                return oldItem;

            } else {
                String message = String.format("Not access to item: %s", oldItem);
                log.warn(message);
                throw new NotAccessException(message);
            }

        } else {
            String message = String.format("An item was not found by id: %s", item);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public void delete(long itemId, long userId) {
        if (items.containsKey(itemId)) {
            Item item = items.get(itemId);

            if (item.getOwnerId() == userId) {
                items.remove(itemId);
            } else {
                String message = String.format("Not access to item: %s", itemId);
                log.warn(message);
                throw new NotAccessException(message);
            }

        } else {
            String message = String.format("An item was not found by id: %d", itemId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public List<Item> searchByText(String text) {
        return items.values().stream()
                .filter(Item::isAvailable)
                .filter(item ->
                        item.getName().equalsIgnoreCase(text)
                        || item.getDescription().equalsIgnoreCase(text))
                .toList();
    }

    private long sequenceId = 0;

    private long generateId() {
        return ++sequenceId;
    }
}
