package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * Controller for users
 */

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final String USER_ID = "user-id";
    private static final String PATH_USER_ID = "/{user-id}";

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("Request: create user: {}", userDto);
        return userService.create(userDto);
    }

    @GetMapping
    public List<UserDto> findAll() {
        log.info("Request: find all users");
        return userService.findAll();
    }

    @GetMapping(PATH_USER_ID)
    public UserDto findById(@PathVariable(USER_ID) long userId) {
        log.info("Request: find user by id: {}", userId);
        return userService.findById(userId);
    }

    @PatchMapping(PATH_USER_ID)
    public UserDto update(@PathVariable(USER_ID) long userId,
                          @RequestBody UserDto userDto) {
        userDto.setId(userId);
        log.info("Request: update user: {}", userDto);
        return userService.update(userDto);
    }

    @DeleteMapping(PATH_USER_ID)
    public void delete(@PathVariable(USER_ID) long userId) {
        log.info("Request: delete user by id: {}", userId);
        userService.delete(userId);
    }
}
