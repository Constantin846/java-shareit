package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private static final String BOOKING_ID = "booking-id";
    private static final String PATH_BOOKING_ID = "/{booking-id}";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestBody BookingDtoRequest bookingDto,
                             @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Request: create booking {} by user {}", bookingDto, userId);
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping(PATH_BOOKING_ID)
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approve(@PathVariable(BOOKING_ID) long bookingId,
                              @RequestParam("approved") Boolean approved,
                              @RequestHeader(X_SHARER_USER_ID) long userId) {
       log.info("Request: set approve status={} to booking with id={} by user={}", approved, bookingId, userId);

        BookingStatus status;
        if (approved) {
            status = BookingStatus.APPROVED;
        } else {
            status = BookingStatus.REJECTED;
        }
        return bookingService.setStatus(bookingId, status, userId);
    }

    @GetMapping(PATH_BOOKING_ID)
    @ResponseStatus(HttpStatus.OK)
    public BookingDto findById(@PathVariable(BOOKING_ID) long bookingId,
                               @RequestHeader(X_SHARER_USER_ID) long userId) {
       log.info("Request: find booking with id={} by user={}", bookingId, userId);
        return bookingService.getByIdWithUser(bookingId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findByBooker(
            @RequestParam(name = "state", required = false, defaultValue = "ALL") String state,
            @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Request: find booking with state {} by booker={}", state, userId);
        BookingState bookingState = BookingState.valueOf(state);
        return bookingService.findByBooker(bookingState, userId);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findByOwner(
            @RequestParam(name = "state", required = false, defaultValue = "ALL") String state,
            @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Request: find booking with state {} by booker={}", state, userId);
        BookingState bookingState = BookingState.valueOf(state);
        List<BookingDto> bookings = bookingService.findByOwner(bookingState, userId);

        if (bookings.isEmpty()) {
            String message = String.format("Nothing found by user id: %s", userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
        return bookings;
    }
}
