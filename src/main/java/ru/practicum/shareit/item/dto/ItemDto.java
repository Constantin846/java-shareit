package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.validation.BooleanValue;

/**
 * TODO Sprint add-controllers.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id;
    @NotBlank(message = "Item's name must not be blank")
    String name;
    @NotBlank(message = "Item's description must not be blank")
    String description;
    @BooleanValue(message = "Item's available must be true or false")
    String available;
    Long ownerId;
}
