package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface BookingJpaRepository extends JpaRepository<Booking, Long> {
    String SELECT_BY_BOOKER_ID = """
            select b
            from Booking as b
            join User as u on b.booker = u
            where u.id = ?1
            """;
    String ORDER_BY_START_DESC = "order by b.start desc";

    @Query(SELECT_BY_BOOKER_ID +
            ORDER_BY_START_DESC)
    List<Booking> findAllByBookerId(long bookerId);

    @Query(SELECT_BY_BOOKER_ID +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            ORDER_BY_START_DESC)
    List<Booking> findCurrentByBookerId(long bookerId, Timestamp timestamp);

    @Query(SELECT_BY_BOOKER_ID +
            "and b.end < ?2 " +
            ORDER_BY_START_DESC)
    List<Booking> findPastByBookerId(long bookerId, Timestamp timestamp);

    @Query(SELECT_BY_BOOKER_ID +
            "and b.start > ?2 " +
            ORDER_BY_START_DESC)
    List<Booking> findFutureByBookerId(long bookerId, Timestamp timestamp);

    @Query(SELECT_BY_BOOKER_ID +
            "and b.status = ?2 " +
            ORDER_BY_START_DESC)
    List<Booking> findByBookerIdAndStatus(long bookerId, BookingStatus status);

    String SELECT_BY_OWNER_ID = """
            select b
            from Booking as b
            join Item as i on b.item = i
            where i.ownerId = ?1
            """;

    @Query(SELECT_BY_OWNER_ID +
            ORDER_BY_START_DESC)
    List<Booking> findAllByOwnerId(long ownerId);

    @Query(SELECT_BY_OWNER_ID +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            ORDER_BY_START_DESC)
    List<Booking> findCurrentByOwnerId(long ownerId, Timestamp timestamp);

    @Query(SELECT_BY_OWNER_ID +
            "and b.end < ?2 " +
            ORDER_BY_START_DESC)
    List<Booking> findPastByOwnerId(long ownerId, Timestamp timestamp);

    @Query(SELECT_BY_OWNER_ID +
            "and b.start > ?2 " +
            ORDER_BY_START_DESC)
    List<Booking> findFutureByOwnerId(long ownerId, Timestamp timestamp);

    @Query(SELECT_BY_OWNER_ID +
            "and b.status = ?2 " +
            ORDER_BY_START_DESC)
    List<Booking> findByOwnerIdAndStatus(long ownerId, BookingStatus status);

    Optional<Booking> findPastApprovedByBookerIdAndItemId(long bookerId, long itemId);
}
