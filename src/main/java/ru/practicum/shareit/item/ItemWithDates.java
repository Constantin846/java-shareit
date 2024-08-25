package ru.practicum.shareit.item;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Entity
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ItemWithDates extends Item {
    Instant lastBookingEnd;
    Instant nextBookingStart;

    public ItemWithDates(Item item, Instant lastBookingEnd, Instant nextBookingStart) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.available = item.isAvailable();
        this.ownerId = item.getOwnerId();
        this.lastBookingEnd = lastBookingEnd;
        this.nextBookingStart = nextBookingStart;
    }
}
