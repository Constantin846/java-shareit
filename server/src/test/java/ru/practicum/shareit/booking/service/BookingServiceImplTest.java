package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotAccessException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingJpaRepository bookingRepository;
    @Mock
    private ItemJpaRepository itemRepository;
    @Mock
    private UserJpaRepository userRepository;
    @Mock
    private BookingDtoMapper mapper;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void create() {
        long bookerId = 1L;
        Booking booking = new Booking();
        booking.setId(3L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(booking.getStart().plusHours(1));
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(2L);
        User user = new User();
        Item item = new Item();
        item.setAvailable(true);
        BookingDto expectedBookingDto = new BookingDto();

        when(mapper.toBooking(bookingDtoRequest)).thenReturn(booking);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingDtoRequest.getItemId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(mapper.toBookingDto(booking)).thenReturn(expectedBookingDto);

        BookingDto actualBookingDto = bookingService.create(bookingDtoRequest, bookerId);

        assertEquals(expectedBookingDto, actualBookingDto);
    }

    @Test
    void create_whenBookingHasNotSaved_thenReturnNotFoundException() {
        long bookerId = 1L;
        Booking booking = new Booking();
        booking.setId(3L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(booking.getStart().plusHours(1));
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(2L);
        User user = new User();
        Item item = new Item();
        item.setAvailable(true);

        when(mapper.toBooking(bookingDtoRequest)).thenReturn(booking);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingDtoRequest.getItemId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.create(bookingDtoRequest, bookerId));
    }

    @Test
    void create_whenItemIsNotAvailable_thenReturnConflictException() {
        long bookerId = 1L;
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now());
        booking.setEnd(booking.getStart().plusHours(1));
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(2L);
        User user = new User();
        Item item = new Item();
        item.setAvailable(false);

        when(mapper.toBooking(bookingDtoRequest)).thenReturn(booking);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingDtoRequest.getItemId())).thenReturn(Optional.of(item));

        assertThrows(ConflictException.class, () -> bookingService.create(bookingDtoRequest, bookerId));
    }

    @Test
    void create_whenItemNotFound_thenReturnNotFoundException() {
        long bookerId = 1L;
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now());
        booking.setEnd(booking.getStart().plusHours(1));
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(2L);
        User user = new User();

        when(mapper.toBooking(bookingDtoRequest)).thenReturn(booking);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingDtoRequest.getItemId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.create(bookingDtoRequest, bookerId));
    }

    @Test
    void create_whenUserNotFound_thenReturnNotFoundException() {
        long bookerId = 1L;
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now());
        booking.setEnd(booking.getStart().plusHours(1));
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();

        when(mapper.toBooking(bookingDtoRequest)).thenReturn(booking);
        when(userRepository.findById(bookerId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.create(bookingDtoRequest, bookerId));
    }

    @Test
    void create_whenBookingStartEqualsBookingEnd_thenReturnValidationException() {
        long bookerId = 1L;
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now());
        booking.setEnd(booking.getStart());
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();

        when(mapper.toBooking(bookingDtoRequest)).thenReturn(booking);

        assertThrows(ValidationException.class, () -> bookingService.create(bookingDtoRequest, bookerId));
    }

    @Test
    void getById() {
        long bookingId = 1L;
        Booking booking = new Booking();
        BookingDto expectedBookingDto = new BookingDto();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(mapper.toBookingDto(booking)).thenReturn(expectedBookingDto);

        BookingDto actualBookingDto = bookingService.getById(bookingId);

        assertEquals(expectedBookingDto, actualBookingDto);
    }

    @Test
    void getByIdWithUser_whenBookerIdEqualsUserId_thenReturnBookingDto() {
        long bookingId = 1L;
        long userId = 2L;
        User booker = new User();
        booker.setId(userId);
        Booking booking = new Booking();
        booking.setBooker(booker);
        BookingDto expectedBookingDto = new BookingDto();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(mapper.toBookingDto(booking)).thenReturn(expectedBookingDto);

        BookingDto actualBookingDto = bookingService.getByIdWithUser(bookingId, userId);

        assertEquals(expectedBookingDto, actualBookingDto);
    }

    @Test
    void getByIdWithUser_whenItemOwnerIdEqualsUserId_thenReturnBookingDto() {
        long bookingId = 1L;
        long userId = 2L;
        User booker = new User();
        booker.setId(userId + 1);
        Item item = new Item();
        item.setOwnerId(userId);
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        BookingDto expectedBookingDto = new BookingDto();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(mapper.toBookingDto(booking)).thenReturn(expectedBookingDto);

        BookingDto actualBookingDto = bookingService.getByIdWithUser(bookingId, userId);

        assertEquals(expectedBookingDto, actualBookingDto);
    }

    @Test
    void getByIdWithUser_whenItemOwnerIdAndBookerIdDoNotEqualUserId_thenReturnNotAccessException() {
        long bookingId = 1L;
        long userId = 2L;
        User booker = new User();
        booker.setId(userId + 1);
        Item item = new Item();
        item.setOwnerId(userId + 2);
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        BookingDto expectedBookingDto = new BookingDto();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(NotAccessException.class, () -> bookingService.getByIdWithUser(bookingId, userId));
    }

    @Test
    void setStatus() {
        long bookingId = 1L;
        BookingStatus status = BookingStatus.APPROVED;
        long userId = 2L;
        Item item = new Item();
        item.setOwnerId(userId);
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        BookingDto expectedBookingDto = new BookingDto();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(mapper.toBookingDto(booking)).thenReturn(expectedBookingDto);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(mapper.toBookingDto(booking)).thenReturn(expectedBookingDto);

        BookingDto actualBookingDto = bookingService.setStatus(bookingId, status, userId);

        assertEquals(expectedBookingDto, actualBookingDto);
    }

    @Test
    void setStatus_whenItemOwnerIdDoesNotEqualUserId_thenReturnNotAccessException() {
        long bookingId = 1L;
        BookingStatus status = BookingStatus.APPROVED;
        long userId = 2L;
        Item item = new Item();
        item.setOwnerId(userId + 1);
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(NotAccessException.class, () -> bookingService.setStatus(bookingId, status, userId));
    }

    @Test
    void findByBooker_whenStateEqualsAll() {
        BookingState state = BookingState.ALL;
        long bookerId = 1L;

        bookingService.findByBooker(state, bookerId);

        verify(bookingRepository).findAllByBookerId(bookerId);
    }

    /*@Test
    void findByBooker_whenStateEqualsCurrent() {
        BookingState state = BookingState.CURRENT;
        long bookerId = 1L;

        bookingService.findByBooker(state, bookerId);

        verify(bookingRepository).findCurrentByBookerId(bookerId, Timestamp.valueOf(LocalDateTime.now()));
    }*/ //todo

    @Test
    void findByBooker_whenStateEqualsWaiting() {
        BookingState state = BookingState.WAITING;
        long bookerId = 1L;

        bookingService.findByBooker(state, bookerId);

        verify(bookingRepository).findByBookerIdAndStatus(bookerId, BookingStatus.WAITING);
    }

    @Test
    void findByBooker_whenStateEqualsRejected() {
        BookingState state = BookingState.REJECTED;
        long bookerId = 1L;

        bookingService.findByBooker(state, bookerId);

        verify(bookingRepository).findByBookerIdAndStatus(bookerId, BookingStatus.REJECTED);
    }

    @Test
    void findByOwner_whenStateEqualsAll() {
        BookingState state = BookingState.ALL;
        long ownerId = 1L;

        bookingService.findByOwner(state, ownerId);

        verify(bookingRepository).findAllByOwnerId(ownerId);
    }

    /*@Test
    void findByOwner_whenStateEqualsCurrent() {
        BookingState state = BookingState.CURRENT;
        long ownerId = 1L;

        bookingService.findByOwner(state, ownerId);

        verify(bookingRepository).findCurrentByOwnerId(ownerId, Timestamp.valueOf(LocalDateTime.now()));
    }*/ //todo

    @Test
    void findByOwner_whenStateEqualsWaiting() {
        BookingState state = BookingState.WAITING;
        long ownerId = 1L;

        bookingService.findByOwner(state, ownerId);

        verify(bookingRepository).findByOwnerIdAndStatus(ownerId, BookingStatus.WAITING);
    }

    @Test
    void findByOwner_whenStateEqualsRejected() {
        BookingState state = BookingState.REJECTED;
        long ownerId = 1L;

        bookingService.findByOwner(state, ownerId);

        verify(bookingRepository).findByOwnerIdAndStatus(ownerId, BookingStatus.REJECTED);
    }
}