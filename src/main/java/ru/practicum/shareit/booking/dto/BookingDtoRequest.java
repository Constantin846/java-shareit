package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.adapters.InstantDeserializer;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoRequest {
    @NotNull
    Long itemId;

    @NotNull
    @JsonDeserialize(using = InstantDeserializer.class)
    @FutureOrPresent(message = "Start of booking must not be in past")
    Instant start;

    @NotNull
    @JsonDeserialize(using = InstantDeserializer.class)
    @Future(message = "End of booking must be in future")
    Instant end;
}
