package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepository.create(UserDtoMapper.toUser(userDto));
        return findById(user.getId());
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserDtoMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto findById(long userId) {
        User user = userRepository.findById(userId);
        return UserDtoMapper.toUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = userRepository.update(UserDtoMapper.toUser(userDto));
        return findById(user.getId());
    }

    @Override
    public void delete(long userId) {
        userRepository.delete(userId);
    }
}
