package ru.practicum.shareit.item.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

@Entity
@Table(name = "comments")
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = "author")
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "text", nullable = false)
    String text;

    @Column(name = "item_id", nullable = false)
    Long itemId;

    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;
}
