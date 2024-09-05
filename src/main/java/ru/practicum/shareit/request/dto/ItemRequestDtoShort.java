package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ItemRequestDtoShort {
    Long id;

    String description;

    //LocalDateTime date; todo

    LocalDateTime created;

    Long userId;
}
