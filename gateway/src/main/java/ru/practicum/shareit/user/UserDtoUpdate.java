package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.NotBlankOrNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDtoUpdate {
    @Email(message = "Invalid user's email")
    String email;

    @NotBlankOrNull(message = "User's name must be null or not be blank")
    String name;
}