package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exceptions.ValidationException;

/**
 * Controller for users
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserClient userClient;
    private static final String USER_ID = "user-id";
    private static final String PATH_USER_ID = "/{user-id}";

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        log.info("Request: create user: {}", userDto);
        return userClient.post(userDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        log.info("Request: find all users");
        return userClient.findAll();
    }

    @GetMapping(PATH_USER_ID)
    public ResponseEntity<Object> findById(@PathVariable(USER_ID) long userId) {
        log.info("Request: find user by id: {}", userId);
        return userClient.findById(userId);
    }

    @PatchMapping(PATH_USER_ID)
    public ResponseEntity<Object> update(
            @NotNull(message = "The user's id is null") @PathVariable(USER_ID) Long userId,
            @RequestBody UserDtoUpdate userDtoUpdate) {
        log.info("Request: update user {} with id={}", userDtoUpdate, userId);
        if (userId == null) {
            String message = "The user's id is null";
            log.warn(message);
            throw new ValidationException(message);
        }
        return userClient.patch(userId, userDtoUpdate);
    }

    @DeleteMapping(PATH_USER_ID)
    public void delete(@PathVariable(USER_ID) long userId) {
        log.info("Request: delete user by id: {}", userId);
        userClient.delete(userId);
    }
}
