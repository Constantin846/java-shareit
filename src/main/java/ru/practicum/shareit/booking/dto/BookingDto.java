package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;

    ItemDto item;

    LocalDateTime start;

    LocalDateTime end;

    UserDto booker;

    BookingStatus status;
}