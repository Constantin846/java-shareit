package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Comment;

import java.util.Set;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findByItemId(long itemId);
}
