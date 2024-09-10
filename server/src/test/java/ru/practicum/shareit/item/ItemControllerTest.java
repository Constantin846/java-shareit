package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.user.UserController;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.StringManager.X_SHARER_USER_ID;

@WebMvcTest
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;
    @MockBean
    private BookingController bookingController;
    @MockBean
    private ItemRequestController itemRequestController;
    @MockBean
    private UserController userController;

    @SneakyThrows
    @Test
    void create() {
        long userId = 1L;
        ItemDtoCreate itemDtoCreate = new ItemDtoCreate();
        ItemDto itemDto = new ItemDto();
        when(itemService.create(itemDtoCreate, userId)).thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoCreate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void findItemsOfUser() {
        long userId = 1L;

        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).findItemsOfUser(userId);
    }

    @SneakyThrows
    @Test
    void findById() {
        long itemId = 1L;
        mockMvc.perform(get("/items/{id}", itemId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).findItemBookingCommentsById(itemId);
    }

    @SneakyThrows
    @Test
    void update() {
        long itemId = 1L;
        long userId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setId(userId);
        when(itemService.update(itemDto, userId)).thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{id}", itemId)
                        .header(X_SHARER_USER_ID, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void delete() {
        long itemId = 1L;
        long userId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/items/{id}", itemId)
                .header(X_SHARER_USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).delete(itemId, userId);
    }

    @SneakyThrows
    @Test
    void search() {
        String text = "text";
        mockMvc.perform(get(String.format("/items/search?text=%s", text)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService).searchByText(text);
    }

    @SneakyThrows
    @Test
    void search_whenTextIsBlank_thenReturnEmptyList() {
        String text = "  ";
        String result = mockMvc.perform(get(String.format("/items/search?text=%s", text)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ItemDto> emptyList = List.of();
        assertEquals(objectMapper.writeValueAsString(emptyList), result);
    }

    @SneakyThrows
    @Test
    void createComment() {
        long userId = 1L;
        long itemId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setItemId(itemId);
        when(itemService.createComment(commentDto, userId)).thenReturn(commentDto);

        String result = mockMvc.perform(post("/items/{id}/comment", itemId)
                        .header(X_SHARER_USER_ID, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDto), result);
    }
}