package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;

import java.util.List;

//@Mapper(componentModel = "spring")
public interface BookingDtoMapper {
    //BookingDtoMapper MAPPER = Mappers.getMapper(BookingDtoMapper.class);

    BookingDto toBookingDto(Booking booking);

    Booking toBooking(BookingDto bookingDto);

    Booking toBooking(BookingDtoRequest bookingDto);

    List<BookingDto> toBookingDto(List<Booking> bookings);

    List<Booking> toBooking(List<BookingDto> bookings);
}
