package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ItemRequestDtoMapperImplTest {
    @InjectMocks
    private ItemRequestDtoMapper mapper = ItemRequestDtoMapper.MAPPER;
    private ItemRequest request;
    private ItemRequestDto requestDto;
    private List<ItemRequest> requestList;

    @Test
    void toItemRequestDto() {
        createItemRequest();
        ItemRequestDto actualRequestDto = mapper.toItemRequestDto(request);

        assertEquals(requestDto, actualRequestDto);
    }

    @Test
    void toItemRequestDto_whenRequestIsNull_thenReturnNull() {
        ItemRequestDto actualRequestDto = mapper.toItemRequestDto(request);

        assertNull(actualRequestDto);
    }

    @Test
    void toItemRequestDtoShort() {
        createItemRequest();
        ItemRequestDtoShort actualRequestDto = mapper.toItemRequestDto(request);

        assertEquals(requestDto.getId(), actualRequestDto.getId());
    }

    @Test
    void toItemRequest() {
        ItemRequestDtoCreate itemRequestDtoCreate = new ItemRequestDtoCreate();
        itemRequestDtoCreate.setUserId(1L);
        itemRequestDtoCreate.setDescription("description");

        ItemRequest actualRequest = mapper.toItemRequest(itemRequestDtoCreate);

        assertEquals(itemRequestDtoCreate.getUserId(), actualRequest.getUserId());
        assertEquals(itemRequestDtoCreate.getDescription(), actualRequest.getDescription());
    }

    @Test
    void toItemRequestDto_list() {
        createItemRequestList();
        List<ItemRequestDto> requestDtoList = mapper.toItemRequestDto(requestList);

        assertEquals(requestList.getFirst().getId(), requestDtoList.getFirst().getId());
    }

    private void createItemRequest() {
        request = new ItemRequest();
        request.setId(1L);
        request.setDescription("des");
        request.setCreated(LocalDateTime.now());
        request.setUserId(2L);

        Item item = new Item();
        item.setId(7L);
        item.setName("name");
        item.setDescription("string");
        item.setAvailable(true);
        item.setOwnerId(8L);
        Set<Item> itemSet = new HashSet<>();
        itemSet.add(item);
        request.setItems(itemSet);

        requestDto = mapper.toItemRequestDto(request);
    }

    private void createItemRequestList() {
        createItemRequest();
        requestList = new ArrayList<>();
        requestList.add(request);
    }
}