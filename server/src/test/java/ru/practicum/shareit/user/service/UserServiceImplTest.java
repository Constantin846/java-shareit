package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapperImpl;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserJpaRepository userJpaRepository;
    @Mock
    private UserDtoMapperImpl userDtoMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void create() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        UserDto expectedUserDto = userDtoMapper.toUserDto(user);
        when(userDtoMapper.toUser(expectedUserDto)).thenReturn(user);
        when(userJpaRepository.save(user)).thenReturn(user);
        when(userDtoMapper.toUserDto(user)).thenReturn(expectedUserDto);
        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto actualUserDto = userService.create(expectedUserDto);

        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void findAll() {
        List<User> users = List.of(new User());
        List<UserDto> expectedUsersDto = userDtoMapper.toUserDto(users);
        when(userJpaRepository.findAll()).thenReturn(users);
        when(userDtoMapper.toUserDto(users)).thenReturn(expectedUsersDto);

        List<UserDto> actualUsersDto = userService.findAll();

        assertEquals(expectedUsersDto, actualUsersDto);
    }

    @Test
    void findById_whenUserFound_thenReturnUser() {
        long userId = 1L;
        User expectedUser = new User();
        UserDto expectedUserDto = userDtoMapper.toUserDto(expectedUser);
        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        when(userDtoMapper.toUserDto(expectedUser)).thenReturn(expectedUserDto);

        UserDto actualUserDto = userService.findById(userId);

        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void findById_whenUserNotFound_thenReturnNotFoundException() {
        long userId = 1L;
        when(userJpaRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    void update_whenUserUpdate_thenReturnUserDto() {
        long userId = 1L;
        User user = new User();
        user.setEmail("email@mail.com");
        user.setName("name");
        user.setId(userId);
        UserDto expectedUserDto = userDtoMapper.toUserDto(user);
        when(userDtoMapper.toUser(expectedUserDto)).thenReturn(user);
        when(userJpaRepository.save(user)).thenReturn(user);
        when(userDtoMapper.toUserDto(user)).thenReturn(expectedUserDto);
        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto actualUserDto = userService.update(expectedUserDto);

        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void update_whenUserIdIsNull_thenReturnValidationException() {
        User user = new User();
        UserDto userDto = userDtoMapper.toUserDto(user);
        when(userDtoMapper.toUser(userDto)).thenReturn(user);

        assertThrows(ValidationException.class, () -> userService.update(userDto));
    }

    @Test
    void update_whenUserDoesNotExist_thenReturnNotFoundException() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        UserDto userDto = userDtoMapper.toUserDto(user);
        when(userDtoMapper.toUser(userDto)).thenReturn(user);
        when(userJpaRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(userDto));
    }

    @Test
    void delete() {
        long userId = 1L;
        userService.delete(userId);

        verify(userJpaRepository).deleteById(userId);
    }
}