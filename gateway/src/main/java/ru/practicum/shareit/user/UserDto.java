package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO class for user
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotBlank(message = "User's email must not be blank")
    @Email(message = "Invalid user's email")
    String email;

    @NotBlank(message = "User's name must not be blank")
    String name;
}
