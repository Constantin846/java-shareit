package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.BooleanValue;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.NotBlankOrNull;
import ru.practicum.shareit.validation.Update;

/**
 * DTO class for item
 */
@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ItemDto {
    @NotBlank(message = "Item's name must not be blank", groups = Create.class)
    @NotBlankOrNull(message = "Item's name must be null or not be blank", groups = Update.class)
    String name;

    @NotBlank(message = "Item's description must not be blank", groups = Create.class)
    @NotBlankOrNull(message = "Item's description must be null or not be blank", groups = Update.class)
    String description;

    @BooleanValue(message = "Item's available must be true or false", groups = {Create.class, Update.class})
    String available;

    Long ownerId;
}
