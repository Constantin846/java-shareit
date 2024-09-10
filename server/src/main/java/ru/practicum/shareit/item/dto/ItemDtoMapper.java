package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemDtoMapper {
    ItemDtoMapper MAPPER = Mappers.getMapper(ItemDtoMapper.class);

    ItemDto toItemDto(Item item);

    ItemBookingCommentDto toItemBookingCommentDto(Item item);

    Item toItem(ItemDto itemDto);

    Item toItem(ItemDtoCreate itemDtoCreate);

    default List<ItemDto> toItemDto(List<Item> items) {
        return items.stream()
                .map(this::toItemDto)
                .toList();
    }
}
