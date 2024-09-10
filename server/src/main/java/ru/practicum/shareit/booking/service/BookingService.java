package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingDtoRequest bookingDto, long bookerId);

    BookingDto getById(long bookingId);

    BookingDto getByIdWithUser(long bookingId, long userId);

    BookingDto setStatus(long bookingId, BookingStatus status, long userId);

    List<BookingDto> findByBooker(BookingState state, long bookerId);

    List<BookingDto> findByOwner(BookingState state, long ownerId);
}
