package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ItemDtoMapperImplTest {
    @InjectMocks
    private ItemDtoMapper mapper = ItemDtoMapper.MAPPER;

    @Test
    void toItemBookingCommentDto() {
        long id = 1L;
        Item item = new Item();
        item.setId(id);

        ItemBookingCommentDto expectedIBCDto = new ItemBookingCommentDto();
        expectedIBCDto.setId(id);

        ItemBookingCommentDto actualIBCDto = mapper.toItemBookingCommentDto(item);

        assertEquals(expectedIBCDto.getId(), actualIBCDto.getId());
    }

    @Test
    void toItemBookingCommentDto_whenItemIsNull_thenReturnNull() {
        Item item = null;

        ItemBookingCommentDto actualIBCDto = mapper.toItemBookingCommentDto(item);

        assertNull(actualIBCDto);
    }

    @Test
    void toItem() {
        long id = 2L;
        boolean available = true;
        ItemDtoCreate itemDtoCreate = new ItemDtoCreate();
        itemDtoCreate.setId(id);
        itemDtoCreate.setAvailable(Boolean.toString(available));

        Item expectedItem = new Item();
        expectedItem.setId(id);
        expectedItem.setAvailable(available);

        Item actualItem = mapper.toItem(itemDtoCreate);

        assertEquals(expectedItem.getId(), actualItem.getId());
        assertEquals(expectedItem.isAvailable(), actualItem.isAvailable());
    }

    @Test
    void toItem_whenItemDtoCreateIsNull_thenReturnNull() {
        ItemDtoCreate itemDtoCreate = null;

        Item actualItem = mapper.toItem(itemDtoCreate);

        assertNull(actualItem);
    }
}