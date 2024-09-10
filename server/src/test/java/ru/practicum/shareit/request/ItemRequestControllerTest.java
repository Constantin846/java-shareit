package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.request.dto.ItemRequestDtoCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.UserController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestDtoMapper mapper;
    @MockBean
    private ItemRequestService itemRequestService;
    @MockBean
    private BookingController bookingController;
    @MockBean
    private ItemController itemController;
    @MockBean
    private UserController userController;

    @SneakyThrows
    @Test
    void create() {
        long userId = 1L;
        ItemRequestDtoCreate itemRequestDtoCreate = new ItemRequestDtoCreate();
        itemRequestDtoCreate.setUserId(userId);
        ItemRequestDtoShort itemRequestDtoShort = new ItemRequestDtoShort();
        when(itemRequestService.create(itemRequestDtoCreate)).thenReturn(itemRequestDtoShort);

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDtoCreate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDtoShort), result);
    }

    @SneakyThrows
    @Test
    void findByUserId() {
        long userId = 1L;

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService).findByUserId(userId);
    }

    @SneakyThrows
    @Test
    void findAll() {
        long userId = 1L;

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService).findAll();
    }

    @SneakyThrows
    @Test
    void findById() {
        long requestId = 1L;
        mockMvc.perform(get("/requests/{id}", requestId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService).findById(requestId);
    }
}