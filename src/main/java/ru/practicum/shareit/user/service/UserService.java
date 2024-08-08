package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    List<UserDto> findAll();

    UserDto findById(long userId);

    UserDto update(UserDto userDto);

    void delete(long userId);
}
