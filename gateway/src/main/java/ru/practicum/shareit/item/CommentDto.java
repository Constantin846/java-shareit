package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    @NotBlank
    String text;

    Long itemId;

    String authorName;

    boolean created;
}
