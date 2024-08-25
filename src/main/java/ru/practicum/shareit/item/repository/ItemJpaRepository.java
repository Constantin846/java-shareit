package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemJpaRepository extends JpaRepository<Item, Long> {
    /*@Query("select new ItemWithDates(t.i, min(t.end), max(t.start) ) " +
            "from ( " +
                    "select b.end as end, b.start as start, it as i " +
                    "from Booking as b " +
                    "left outer join Item as it on it = b.item " +
                    "where it.ownerId = ?1 " +
                    "and b.end > ?2 " +
                    " " +
                ") as t " +
            "group by t.i ")*/
    List<Item> findByOwnerId(long ownerId);

    @Query("select it " +
            "from Item as it " +
            "where it.available = true " +
            "and (upper(it.name) like upper(concat('%', ?1, '%')) " +
            "or upper(it.description) like upper(concat('%', ?1, '%')))")
    List<Item> findAvailableByNameOrDescriptionLike(String text);
}
