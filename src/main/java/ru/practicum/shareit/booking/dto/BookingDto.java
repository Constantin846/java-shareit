package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;

    ItemDto item;

    //@JsonSerialize(using = InstantSerializer.class)
    LocalDateTime start;

    //@JsonSerialize(using = InstantSerializer.class)
    LocalDateTime end;

    UserDto booker;

    BookingStatus status;
}