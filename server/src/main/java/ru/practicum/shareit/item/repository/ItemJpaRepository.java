package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemJpaRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(long ownerId);

    @Query("select it " +
            "from Item as it " +
            "where it.available = true " +
            "and (upper(it.name) like upper(concat('%', ?1, '%')) " +
            "or upper(it.description) like upper(concat('%', ?1, '%')))")
    List<Item> findAvailableByNameOrDescriptionLike(String text);
}
