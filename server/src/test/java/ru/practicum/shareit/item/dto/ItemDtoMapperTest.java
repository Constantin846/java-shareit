package ru.practicum.shareit.item.dto;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@ExtendWith(MockitoExtension.class)
class ItemDtoMapperTest {
    @InjectMocks
    private ItemDtoMapper mapper = ItemDtoMapper.MAPPER;

    @Test
    void toItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(2L);
        List<ItemDto> itemsDto = List.of(itemDto);
        List<Item> items = itemsDto.stream()
                .map(mapper::toItem)
                .toList();

        List<ItemDto> actualItemDtoList = mapper.toItemDto(items);

        assertArrayEquals(Arrays.array(itemsDto), Arrays.array(actualItemDtoList));
    }
}