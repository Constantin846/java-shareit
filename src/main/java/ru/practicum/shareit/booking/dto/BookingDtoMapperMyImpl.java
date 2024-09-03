package ru.practicum.shareit.booking.dto;

@Deprecated
public class BookingDtoMapperMyImpl {} /*implements BookingDtoMapper {
    @Override
    public BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setItem(ItemDtoMapper.toItemDto(booking.getItem()));
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setBooker(UserDtoMapper.toUserDto(booking.getBooker()));
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    @Override
    public Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setItem(ItemDtoMapper.toItem(bookingDto.getItem()));
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBooker(UserDtoMapper.toUser(bookingDto.getBooker()));
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    @Override
    public Booking toBooking(BookingDtoRequest bookingDto) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }

    @Override
    public List<BookingDto> toBookingDto(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toBookingDto)
                .toList();
    }

    @Override
    public List<Booking> toBooking(List<BookingDto> bookings) {
        return bookings.stream()
                .map(this::toBooking)
                .toList();
    }
}*/