package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

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
    @NotNull(message = "Item's available must be true or false")
    String available;
    Long ownerId;
}
