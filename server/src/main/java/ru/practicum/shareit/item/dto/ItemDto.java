package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

/**
 * DTO class for item
 */
@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ItemDto {
    Long id;

    String name;

    String description;

    String available;

    Long ownerId;
}
