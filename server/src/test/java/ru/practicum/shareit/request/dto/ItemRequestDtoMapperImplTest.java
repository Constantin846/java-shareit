package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;

class ItemRequestDtoMapperImplTest {
    private final ItemRequestDtoMapper mapper = ItemRequestDtoMapper.MAPPER;
    private ItemRequest request;
    private ItemRequestDto requestDto;

    @BeforeEach
    void beforeEach() {
        request = new ItemRequest();
        request.setId(1L);
        request.setDescription("des");
        request.setCreated(LocalDateTime.now());
        request.setUserId(2L);

        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        requestDto.setUserId(request.getUserId());
    }

    @Test
    void toItemRequestDto() {
    }

    @Test
    void toItemRequestDtoShort() {
    }

    @Test
    void toItemRequest() {
    }

    @Test
    void testToItemRequestDto() {
    }

    @Test
    void itemToItemDto() {
    }

    @Test
    void itemSetToItemDtoSet() {
    }
}