package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserDtoMapper MAPPER = Mappers.getMapper(UserDtoMapper.class);

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    List<UserDto> toUserDto(List<User> users);

    List<User> toUser(List<UserDto> users);
}
