package ru.practicum.shareit.item;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.Booking;



@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ItemWithBookings extends Item { // todo delete
    @Column(name = "last_booking")
    Booking lastBooking;

    @Column(name = "next_booking")
    Booking nextBooking;

    public ItemWithBookings(Item item, Booking lastBooking, Booking nextBooking) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.available = item.isAvailable();
        this.ownerId = item.getOwnerId();
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
    }
}
