package ru.practicum.shareit.booking.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingJpaRepository bookingRepository;
    private final ItemJpaRepository itemRepository;
    private final UserJpaRepository userRepository;
    private final BookingDtoMapper bookingDtoMapper;

    @Override
    @Transactional
    public BookingDto create(BookingDtoRequest bookingDto, long bookerId) {
        Booking booking = bookingDtoMapper.toBooking(bookingDto);

        if (booking.getStart().equals(booking.getEnd())) {
            String message = String.format("Start and end of booking must not be equals: %s", booking);
            log.warn(message);
            throw new ValidationException(message);
        }

        Optional<User> userOp = userRepository.findById(bookerId);
        User booker = userOp.orElseThrow(() -> {
            String message = String.format("User was not found by id: %d", bookerId);
            log.warn(message);
            return new NotFoundException(message);
        });
        booking.setBooker(booker);

        Optional<Item> itemOp = itemRepository.findById(bookingDto.getItemId());
        Item item = itemOp.orElseThrow(() -> {
            String message = String.format("Item was not found by id: %d", bookingDto.getItemId());
            log.warn(message);
            return new NotFoundException(message);
        });
        if (!item.isAvailable()) {
            String message = String.format("Item is not available: %s", item);
            log.warn(message);
            throw new ConflictException(message);
        }
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        Booking newBooking = bookingRepository.save(booking);
        return getById(newBooking.getId());
    }

    @Override
    public BookingDto getById(long bookingId) {
        Booking booking = getBookingById(bookingId);
        return bookingDtoMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getByIdWithUser(long bookingId, long userId) {
        Booking booking = getBookingById(bookingId);

        if (booking.getBooker().getId() != userId && booking.getItem().getOwnerId() != userId) {
            String message = String.format("Not access to booking: %s", bookingId);
            log.warn(message);
            throw new NotAccessException(message);
        }
        return bookingDtoMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto setStatus(long bookingId, BookingStatus status, long userId) {
        Booking booking = getBookingById(bookingId);

        if (booking.getItem().getOwnerId() != userId) {
            String message = String.format("Not access to item: %s", booking.getItem());
            log.warn(message);
            throw new NotAccessException(message);
        }

        booking.setStatus(status);
        bookingRepository.save(booking);
        return getById(bookingId);
    }

    @Override
    public List<BookingDto> findByBooker(BookingState state, long bookerId) {
        switch (state) {
            case ALL -> {
                return bookingDtoMapper.toBookingDto(
                        bookingRepository.findAllByBookerId(bookerId)
                );
            }
            case CURRENT -> {
                return bookingDtoMapper.toBookingDto(
                        bookingRepository.findCurrentByBookerId(bookerId, Timestamp.valueOf(LocalDateTime.now()))
                );
            }
            case PAST -> {
                return bookingDtoMapper.toBookingDto(
                        bookingRepository.findPastByBookerId(bookerId, Timestamp.valueOf(LocalDateTime.now()))
                );
            }
            case FUTURE -> {
                return bookingDtoMapper.toBookingDto(
                        bookingRepository.findFutureByBookerId(bookerId, Timestamp.valueOf(LocalDateTime.now()))
                );
            }
            case WAITING -> {
                return bookingDtoMapper.toBookingDto(
                        bookingRepository.findByBookerIdAndStatus(bookerId, BookingStatus.WAITING)
                );
            }
            case REJECTED -> {
                return bookingDtoMapper.toBookingDto(
                        bookingRepository.findByBookerIdAndStatus(bookerId, BookingStatus.REJECTED)
                );
            }
        }
        String message = String.format("Not define what should be found by booker id: %s", bookerId);
        log.warn(message);
        throw new NotFoundException(message);
    }

    @Override
    public List<BookingDto> findByOwner(BookingState state, long ownerId) {
        switch (state) {
            case ALL -> {
                return bookingDtoMapper.toBookingDto(
                        bookingRepository.findAllByOwnerId(ownerId)
                );
            }
            case CURRENT -> {
                return bookingDtoMapper.toBookingDto(
                        bookingRepository.findCurrentByOwnerId(ownerId, Timestamp.valueOf(LocalDateTime.now()))
                );
            }
            case PAST -> {
                return bookingDtoMapper.toBookingDto(
                        bookingRepository.findPastByOwnerId(ownerId, Timestamp.valueOf(LocalDateTime.now()))
                );
            }
            case FUTURE -> {
                return bookingDtoMapper.toBookingDto(
                        bookingRepository.findFutureByOwnerId(ownerId, Timestamp.valueOf(LocalDateTime.now()))
                );
            }
            case WAITING -> {
                return bookingDtoMapper.toBookingDto(
                        bookingRepository.findByOwnerIdAndStatus(ownerId, BookingStatus.WAITING)
                );
            }
            case REJECTED -> {
                return bookingDtoMapper.toBookingDto(
                        bookingRepository.findByOwnerIdAndStatus(ownerId, BookingStatus.REJECTED)
                );
            }
        }
        String message = String.format("Not define what should be found by owner id: %s", ownerId);
        log.warn(message);
        throw new NotFoundException(message);
    }

    private Booking getBookingById(long bookingId) {
        Optional<Booking> bookingOp = bookingRepository.findById(bookingId);
        return bookingOp.orElseThrow(() -> {
            String message = String.format("Booking was not found by id: %d", bookingId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }
}
