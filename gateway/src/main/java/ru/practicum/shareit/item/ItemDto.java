package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.validation.BooleanValue;

/**
 * DTO class for item
 */
@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ItemDto {
    @NotBlank(message = "Item's name must not be blank")
    String name;

    @NotBlank(message = "Item's description must not be blank")
    String description;

    @BooleanValue(message = "Item's available must be true or false")
    String available;

    Long ownerId;
}
