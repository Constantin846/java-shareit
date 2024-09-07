package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestDtoMapper {
    ItemRequestDtoMapper MAPPER = Mappers.getMapper(ItemRequestDtoMapper.class);

    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    ItemRequestDtoShort toItemRequestDtoShort(ItemRequest itemRequest);

    ItemRequest toItemRequest(ItemRequestDtoCreate itemRequestDtoCreate);

    List<ItemRequestDto> toItemRequestDto(List<ItemRequest> itemRequests);

    default List<ItemRequestDtoShort> toItemRequestDtoShort(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(this::toItemRequestDtoShort)
                .toList();
    }
}
