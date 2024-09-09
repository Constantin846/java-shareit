package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class BookingDtoMapperImplTest {
    @InjectMocks
    private BookingDtoMapper mapper = BookingDtoMapper.MAPPER;
    private Booking booking;
    private BookingDto bookingDto;

    @Test
    void toBookingDto() {
        createBookings();

        BookingDto actualBookingDto = mapper.toBookingDto(booking);

        assertEquals(bookingDto.getId(), actualBookingDto.getId());
    }

    @Test
    void toBookingDto_whenBookingIsNull_thenReturnNull() {
        booking = null;

        BookingDto actualBookingDto = mapper.toBookingDto(booking);

        assertNull(actualBookingDto);
    }

    @Test
    void toBooking() {
        createBookings();

        Booking actualBooking = mapper.toBooking(bookingDto);

        assertEquals(booking.getId(), actualBooking.getId());
    }

    @Test
    void toBooking_whenBookingDtoIsNull_thenReturnNull() {
        bookingDto = null;

        Booking actualBooking = mapper.toBooking(bookingDto);

        assertNull(actualBooking);
    }

    @Test
    void toBookingFromBookingDtoRequest() {
        Booking expectedBooking = new Booking();
        expectedBooking.setStart(LocalDateTime.now());
        expectedBooking.setEnd(expectedBooking.getStart().plusHours(1));

        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setStart(expectedBooking.getStart());
        bookingDtoRequest.setEnd(expectedBooking.getEnd());

        Booking actualBooking = mapper.toBooking(bookingDtoRequest);

        assertEquals(expectedBooking.getStart(), actualBooking.getStart());
        assertEquals(expectedBooking.getEnd(), actualBooking.getEnd());
    }

    @Test
    void testToBookingDto() {
        createBookings();
        List<Booking> bookingList = List.of(booking);
        List<BookingDto> bookingDtoList = List.of(bookingDto);

        List<BookingDto> actualBookingDtoList = mapper.toBookingDto(bookingList);

        assertArrayEquals(bookingDtoList.toArray(), actualBookingDtoList.toArray());
    }

    private void createBookings() {
        long id = 1L;
        boolean available = true;
        booking = new Booking();
        booking.setId(id);
        Item item = new Item();
        item.setAvailable(available);
        booking.setItem(item);
        booking.setBooker(new User());

        bookingDto = new BookingDto();
        bookingDto.setId(id);
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(Boolean.toString(available));
        bookingDto.setItem(itemDto);
        bookingDto.setBooker(new UserDto());
    }
}