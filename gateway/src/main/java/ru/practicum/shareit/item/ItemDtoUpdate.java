package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.BooleanValue;
import ru.practicum.shareit.validation.NotBlankOrNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoUpdate {
    @NotBlankOrNull(message = "Item's name must be null or not be blank")
    String name;

    @NotBlankOrNull(message = "Item's description must be null or not be blank")
    String description;

    @BooleanValue(message = "Item's available must be true or false")
    String available;

    Long ownerId;
}
