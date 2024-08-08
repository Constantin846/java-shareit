package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.info("Request: create user: {}", userDto);
        return userService.create(userDto);
    }

    @GetMapping
    public List<UserDto> findAll() {
        log.info("Request: find all users");
        return userService.findAll();
    }

    @GetMapping("/{user-id}")
    public UserDto findById(@PathVariable("user-id") long userId) {
        log.info("Request: find user by id: {}", userId);
        return userService.findById(userId);
    }

    @PatchMapping("/{user-id}")
    public UserDto update(@PathVariable("user-id") long userId,
                          @RequestBody UserDto userDto) {
        userDto.setId(userId);
        log.info("Request: update user: {}", userDto);
        if (userDto.getId() == null) {
            String message = String.format("The user's id is null: %s", userDto);
            log.warn(message);
            throw new ValidationException(message);
        }
        return userService.update(userDto);
    }

    @DeleteMapping("/{user-id}")
    public void delete(@PathVariable("user-id") long userId) {
        log.info("Request: delete user by id: {}", userId);
        userService.delete(userId);
    }
}
