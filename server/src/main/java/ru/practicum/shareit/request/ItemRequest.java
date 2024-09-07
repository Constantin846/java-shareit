package ru.practicum.shareit.request;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO Sprint add-item-requests.
 */

@Entity
@Table(name = "item_requests")
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = "items")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "date"/*, nullable = false*/)
    LocalDateTime date;

    @Column(name = "create_date", nullable = false)
    LocalDateTime created;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @OneToMany(fetch = FetchType.LAZY/*, mappedBy = "requestId"*/)
    @JoinColumn(name = "request_id")
    Set<Item> items = new HashSet<>();
}
