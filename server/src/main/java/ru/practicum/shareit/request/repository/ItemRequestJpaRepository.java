package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRequestJpaRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByUserId(long userId, Sort sort);
}
