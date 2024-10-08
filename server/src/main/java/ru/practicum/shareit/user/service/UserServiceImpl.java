package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserJpaRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = userRepository.save(userDtoMapper.toUser(userDto));
        return findById(user.getId());
    }

    @Override
    public List<UserDto> findAll() {
        return userDtoMapper.toUserDto(userRepository.findAll());
    }

    @Override
    public UserDto findById(long userId) {
        Optional<User> userOp = userRepository.findById(userId);
        User user = userOp.orElseThrow(() -> {
            String message = String.format("User was not found by id: %d", userId);
            log.warn(message);
            return new NotFoundException(message);
        });
        return userDtoMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) {
        User user = userDtoMapper.toUser(userDto);

        if (user.getId() == null) {
            String message = String.format("The user's id is null: %s", user);
            log.warn(message);
            throw new ValidationException(message);
        }

        Optional<User> userOp = userRepository.findById(user.getId());
        User oldUser = userOp.orElseThrow(() -> {
            String message = String.format("User does not exist: %s", user);
            log.warn(message);
            return new NotFoundException(message);
        });

        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }

        User newUser = userRepository.save(oldUser);
        return findById(newUser.getId());
    }

    @Override
    @Transactional
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }
}
