package ru.practicum.shareit.user.dto;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserDtoMapperImplTest {
    private final UserDtoMapper mapper = UserDtoMapper.MAPPER;
    private User user;
    private UserDto userDto;
    private List<User> userList;
    private List<UserDto> userDtoList;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setId(1L);
        user.setEmail("email@em.em");
        user.setName("name");

        userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
    }

    @Test
    void toUserDto() {
        UserDto actualUserDto = mapper.toUserDto(user);

        assertEquals(userDto, actualUserDto);
    }

    @Test
    void toUserDto_whenUserIsNull_thenReturnNull() {
        UserDto actualUserDto = mapper.toUserDto((User) null);

        assertNull(actualUserDto);
    }

    @Test
    void toUser() {
        User actualUser = mapper.toUser(userDto);

        assertEquals(user, actualUser);
    }

    @Test
    void toUser_whenUserDtoIsNull_thenReturnNull() {
        User actualUser = mapper.toUser((UserDto) null);

        assertNull(actualUser);
    }

    @Test
    void toUserDto_list() {
        createLists();
        List<UserDto> actualUserDtoList = mapper.toUserDto(userList);

        assertArrayEquals(Arrays.array(userDtoList), Arrays.array(actualUserDtoList));
    }

    @Test
    void toUserDto_list_whenUserListIsNull_thenReturnNull() {
        List<UserDto> actualUserDtoList = mapper.toUserDto((List<User>) null);

        assertNull(actualUserDtoList);
    }

    @Test
    void toUser_list() {
        createLists();
        List<User> actualUserList = mapper.toUser(userDtoList);

        assertArrayEquals(Arrays.array(userList), Arrays.array(actualUserList));
    }

    @Test
    void toUser_list_whenUserDtoListIsNull_thenReturnNull() {
        List<User> actualUserList = mapper.toUser((List<UserDto>) null);

        assertNull(actualUserList);
    }

    private void createLists() {
        userList = List.of(user);
        userDtoList = List.of(userDto);
    }
}