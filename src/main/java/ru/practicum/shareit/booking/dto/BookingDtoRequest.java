package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoRequest {
    @NotNull
    Long itemId;

    @NotNull
    @FutureOrPresent(message = "Start of booking must not be in past")
    LocalDateTime start;

    @NotNull
    @Future(message = "End of booking must be in future")
    LocalDateTime end;
}
