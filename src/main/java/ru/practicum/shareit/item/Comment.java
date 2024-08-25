package ru.practicum.shareit.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "comments", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "text", nullable = false, length = 255)
    String text;

    @Column(name = "item_id", nullable = false)
    Long itemId;

    @Column(name = "author_id")
    Long authorId;
}
