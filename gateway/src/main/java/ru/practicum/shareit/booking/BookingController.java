package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingState;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;
	private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
	private static final String BOOKING_ID = "booking-id";
	private static final String PATH_BOOKING_ID = "/{booking-id}";

	@GetMapping("/part")
	public ResponseEntity<Object> getBookings(@RequestHeader(X_SHARER_USER_ID) long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader(X_SHARER_USER_ID) long userId,
			@RequestBody @Valid BookingDtoRequest requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping(PATH_BOOKING_ID)
	public ResponseEntity<Object> getBooking(@RequestHeader(X_SHARER_USER_ID) long userId,
			@PathVariable(BOOKING_ID) Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping(PATH_BOOKING_ID)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> approve(@PathVariable(BOOKING_ID) long bookingId,
							  @RequestParam("approved") Boolean approved,
							  @RequestHeader(X_SHARER_USER_ID) long userId) {
		log.info("Request: set approve status={} to booking with id={} by user={}", approved, bookingId, userId);
		return bookingClient.patchSetStatus(bookingId, userId, approved);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> findByBooker(
			@RequestParam(name = "state", required = false, defaultValue = "ALL") String state,
			@RequestHeader(X_SHARER_USER_ID) long userId) {
		log.info("Request: find booking with state {} by booker={}", state, userId);
		return bookingClient.findByUser("", userId, state);
	}

	@GetMapping("/owner")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> findByOwner(
			@RequestParam(name = "state", required = false, defaultValue = "ALL") String state,
			@RequestHeader(X_SHARER_USER_ID) long userId) {
		log.info("Request: find booking with state {} by owner={}", state, userId);
		return bookingClient.findByUser("/owner", userId, state);
	}
}
