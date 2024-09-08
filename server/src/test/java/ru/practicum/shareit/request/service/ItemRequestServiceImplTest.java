package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.request.dto.ItemRequestDtoShort;
import ru.practicum.shareit.request.repository.ItemRequestJpaRepository;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private UserJpaRepository userJpaRepository;
    @Mock
    private ItemRequestJpaRepository itemRequestJpaRepository;
    @Mock
    private ItemRequestDtoMapper mapper;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void create() {
        long userId = 1L;
        long requestId = 1L;
        ItemRequestDtoCreate requestDtoCreate = new ItemRequestDtoCreate();
        ItemRequestDto expectedRequestDto = new ItemRequestDto();
        expectedRequestDto.setUserId(userId);
        expectedRequestDto.setId(requestId);
        requestDtoCreate.setUserId(userId);
        ItemRequest request = new ItemRequest();
        request.setUserId(requestId);
        request.setId(requestId);
        when(mapper.toItemRequest(requestDtoCreate)).thenReturn(request);
        when(itemRequestJpaRepository.save(request)).thenReturn(request);
        when(mapper.toItemRequestDto(request)).thenReturn(expectedRequestDto);
        when(itemRequestJpaRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(userJpaRepository.existsById(requestId)).thenReturn(true);

        ItemRequestDtoShort actualRequestDto = itemRequestService.create(requestDtoCreate);

        assertEquals(expectedRequestDto, actualRequestDto);
    }

    @Test
    void create_whenUserDoesNotExist_thenReturnNotFoundException() {
        long userId = 1L;
        long requestId = 1L;
        ItemRequestDtoCreate requestDtoCreate = new ItemRequestDtoCreate();
        requestDtoCreate.setUserId(userId);
        ItemRequest request = new ItemRequest();
        request.setUserId(requestId);
        request.setId(requestId);
        when(mapper.toItemRequest(requestDtoCreate)).thenReturn(request);
        when(userJpaRepository.existsById(requestId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemRequestService.create(requestDtoCreate));
    }

    @Test
    void findByUserId() {
        long userId = 1L;
        List<ItemRequest> list = List.of(new ItemRequest());
        List<ItemRequestDto> expectedList = mapper.toItemRequestDto(list);
        Sort sort = Sort.by("created").descending();

        when(itemRequestJpaRepository.findByUserId(userId, sort)).thenReturn(list);
        when(mapper.toItemRequestDto(list)).thenReturn(expectedList);

        List<ItemRequestDto> actualList = itemRequestService.findByUserId(userId);

        assertEquals(expectedList, actualList);
    }

    @Test
    void findAll() {
        List<ItemRequest> itemRequests = List.of(new ItemRequest());
        List<ItemRequestDtoShort> expectedRequests = mapper.toItemRequestDtoShort(itemRequests);
        Sort sort = Sort.by("created").descending();
        when(itemRequestJpaRepository.findAll(sort)).thenReturn(itemRequests);
        when(mapper.toItemRequestDtoShort(itemRequests)).thenReturn(expectedRequests);

        List<ItemRequestDtoShort> actualRequests = itemRequestService.findAll();

        assertEquals(expectedRequests, actualRequests);
    }

    @Test
    void findById() {
        long requestId = 1L;
        ItemRequest expectedRequest = new ItemRequest();
        ItemRequestDto expectedRequestDto = mapper.toItemRequestDto(expectedRequest);
        when(itemRequestJpaRepository.findById(requestId)).thenReturn(Optional.of(expectedRequest));
        when(mapper.toItemRequestDto(expectedRequest)).thenReturn(expectedRequestDto);

        ItemRequestDto actualRequestDto = itemRequestService.findById(requestId);

        assertEquals(expectedRequestDto, actualRequestDto);
    }

    @Test
    void findById_whenItemRequestNotFound_thenReturnNotFoundException() {
        long requestId = 1L;
        when(itemRequestJpaRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.findById(requestId));
    }
}