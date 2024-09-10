package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.NotBlankOrNull;
import ru.practicum.shareit.validation.Update;

/**
 * DTO class for user
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotBlank(message = "User's email must not be blank", groups = Create.class)
    @Email(message = "Invalid user's email", groups = {Create.class, Update.class})
    String email;

    @NotBlank(message = "User's name must not be blank", groups = Create.class)
    @NotBlankOrNull(message = "User's name must be null or not be blank", groups = Update.class)
    String name;
}
