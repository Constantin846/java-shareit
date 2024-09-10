package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemRequestDtoMapperTest {
    @InjectMocks
    private ItemRequestDtoMapper mapper = ItemRequestDtoMapper.MAPPER;

    @Test
    void toItemRequestDtoShort() {
        long id = 2L;
        ItemRequestDtoShort itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(id);
        List<ItemRequestDtoShort> requestsDtoSort = List.of(itemRequestDto);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(id);
        List<ItemRequest> itemRequests = List.of(itemRequest);

        List<ItemRequestDtoShort> actualRequestsDtoSort = mapper.toItemRequestDtoShort(itemRequests);

        assertEquals(requestsDtoSort.getFirst().getId(), actualRequestsDtoSort.getFirst().getId());
    }
}