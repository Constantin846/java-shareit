package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemController;
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

@WebMvcTest
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private ItemController itemController;
    @MockBean
    private ItemRequestController itemRequestController;
    @MockBean
    private UserController userController;


    @SneakyThrows
    @Test
    void create() {
        long userId = 1L;
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        BookingDto bookingDto = new BookingDto();
        when(bookingService.create(bookingDtoRequest, userId)).thenReturn(bookingDto);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDtoRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void approve_setApproved() {
        long userId = 1L;
        long bookingId = 1L;
        Boolean approved = true;
        BookingStatus status = BookingStatus.APPROVED;
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStatus(status);
        when(bookingService.setStatus(bookingId, status, userId)).thenReturn(bookingDto);

        String result = mockMvc.perform(patch(String.format("/bookings/{id}?approved=%s", approved), bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void approve_setRejected() {
        long userId = 1L;
        long bookingId = 1L;
        Boolean approved = false;
        BookingStatus status = BookingStatus.REJECTED;
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStatus(status);
        when(bookingService.setStatus(bookingId, status, userId)).thenReturn(bookingDto);

        String result = mockMvc.perform(patch(String.format("/bookings/{id}?approved=%s", approved), bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void approve_whenApprovedParamIsNotBoolean_thenReturnInternalServerError() {
        long userId = 1L;
        long bookingId = 1L;
        String approved = "NotBoolean";

        mockMvc.perform(patch(String.format("/bookings/{id}?approved=%s", approved), bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void findById() {
        long userId = 1L;
        long bookingId = 1L;

        mockMvc.perform(get("/bookings/{id}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).getByIdWithUser(bookingId, userId);
    }

    @SneakyThrows
    @Test
    void findByBooker() {
        long userId = 1L;
        BookingState bookingState = BookingState.ALL;

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService).findByBooker(bookingState, userId);
    }

    @SneakyThrows
    @Test
    void findByOwner() {
        long userId = 1L;
        BookingState bookingState = BookingState.ALL;
        List<BookingDto> expectedList = List.of(new BookingDto());
        when(bookingService.findByOwner(bookingState, userId)).thenReturn(expectedList);

        String result = mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedList), result);
    }

    @SneakyThrows
    @Test
    void findByOwner_whenNothingFound_thenReturnNotFound() {
        long userId = 1L;
        BookingState bookingState = BookingState.ALL;
        List<BookingDto> expectedList = List.of();
        when(bookingService.findByOwner(bookingState, userId)).thenReturn(expectedList);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}