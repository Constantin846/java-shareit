package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.exceptions.NotAccessException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoMapper;
import ru.practicum.shareit.item.dto.ItemBookingCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentJpaRepository;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.request.repository.ItemRequestJpaRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private BookingJpaRepository bookingJpaRepository;
    @Mock
    private CommentJpaRepository commentJpaRepository;
    @Mock
    private ItemJpaRepository itemJpaRepository;
    @Mock
    private ItemRequestJpaRepository itemRequestJpaRepository;
    @Mock
    private UserJpaRepository userJpaRepository;
    @Mock
    private ItemDtoMapper mapper;
    @Mock
    private CommentDtoMapper commentDtoMapper;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void create() {
        long userId = 1L;
        long itemId = 1L;
        ItemDtoCreate itemDtoCreate = new ItemDtoCreate();
        Item item = new Item();
        item.setId(itemId);
        ItemDto expectedItemDto = new ItemDto();
        expectedItemDto.setId(itemId);

        when(userJpaRepository.existsById(userId)).thenReturn(true);
        when(mapper.toItem(itemDtoCreate)).thenReturn(item);
        when(itemJpaRepository.save(item)).thenReturn(item);
        when(itemJpaRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(mapper.toItemDto(item)).thenReturn(expectedItemDto);

        ItemDto actualItemDto = itemService.create(itemDtoCreate, userId);

        assertEquals(expectedItemDto, actualItemDto);
    }

    @Test
    void create_whenUserDoesNotExist_thenReturnNotFoundException() {
        long userId = 1L;
        ItemDtoCreate itemDtoCreate = new ItemDtoCreate();
        when(userJpaRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemService.create(itemDtoCreate, userId));
    }

    @Test
    void create_whenItemRequestDoesNotExist_thenReturnNotFoundException() {
        long userId = 1L;
        long requestId = 1L;
        ItemDtoCreate itemDtoCreate = new ItemDtoCreate();
        itemDtoCreate.setRequestId(requestId);
        when(itemRequestJpaRepository.existsById(requestId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemService.create(itemDtoCreate, userId));
    }

    @Test
    void findItemsOfUser() {
        long userId = 1L;
        List<Item> items = List.of(new Item());
        List<ItemDto> expectedItemDto = mapper.toItemDto(items);

        when(userJpaRepository.existsById(userId)).thenReturn(true);
        when(itemJpaRepository.findByOwnerId(userId)).thenReturn(items);
        when(mapper.toItemDto(items)).thenReturn(expectedItemDto);

        List<ItemDto> actualItemsDto = itemService.findItemsOfUser(userId);

        assertEquals(expectedItemDto, actualItemsDto);
    }

    @Test
    void findItemsOfUser_whenUserDoesNotExist_thenReturnNotFoundException() {
        long userId = 1L;
        when(userJpaRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemService.findItemsOfUser(userId));
    }

    @Test
    void findById() {
        long itemId = 1L;
        Item item = new Item();
        ItemDto expectedItemDto = new ItemDto();
        when(itemJpaRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(mapper.toItemDto(item)).thenReturn(expectedItemDto);

        ItemDto actualItemDto = itemService.findById(itemId);

        assertEquals(expectedItemDto, actualItemDto);
    }

    @Test
    void findById_whenItemNotFound_thenReturnNotFoundException() {
        long itemId = 1L;
        when(itemJpaRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.findById(itemId));
    }

    @Test
    void findItemBookingCommentsById() {
        long itemId = 1L;
        Item item = new Item();
        ItemBookingCommentDto expectedIBCDto = new ItemBookingCommentDto();
        expectedIBCDto.setId(itemId);
        Set<Comment> comments = Set.of(new Comment());
        Set<CommentDto> commentsDto = Set.of(new CommentDto());

        when(itemJpaRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(mapper.toItemBookingCommentDto(item)).thenReturn(expectedIBCDto);
        when(commentJpaRepository.findByItemId(itemId)).thenReturn(comments);
        when(commentDtoMapper.toCommentDto(comments)).thenReturn(commentsDto);

        ItemBookingCommentDto actualIBCDto = itemService.findItemBookingCommentsById(itemId);

        assertEquals(expectedIBCDto, actualIBCDto);
    }

    @Test
    void update() {
        long userId = 1L;
        long itemId = 1L;
        Item item = new Item();
        item.setOwnerId(userId);
        item.setId(itemId);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        ItemDto expectedItemDto = new ItemDto();
        expectedItemDto.setId(itemId);

        when(mapper.toItem(expectedItemDto)).thenReturn(item);
        when(itemJpaRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userJpaRepository.existsById(userId)).thenReturn(true);
        when(itemJpaRepository.save(item)).thenReturn(item);
        when(mapper.toItemDto(item)).thenReturn(expectedItemDto);

        ItemDto actualItemDto = itemService.update(expectedItemDto, userId);

        assertEquals(expectedItemDto, actualItemDto);
    }

    @Test
    void update_whenUserIdDoesNotEqualOwnerId_thenReturnNotFoundException() {
        long userId = 1L;
        long itemId = 1L;
        Item item = new Item();
        item.setOwnerId(userId + 1);
        item.setId(itemId);
        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);

        when(mapper.toItem(itemDto)).thenReturn(item);
        when(itemJpaRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userJpaRepository.existsById(userId)).thenReturn(true);

        assertThrows(NotFoundException.class, () -> itemService.update(itemDto, userId));
    }

    @Test
    void update_whenUserDoesNotExist_thenReturnNotFoundException() {
        long userId = 1L;
        long itemId = 1L;
        Item item = new Item();
        item.setOwnerId(userId);
        item.setId(itemId);
        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);

        when(mapper.toItem(itemDto)).thenReturn(item);
        when(userJpaRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemService.update(itemDto, userId));
    }

    @Test
    void update_whenItemIdIsNull_thenReturnValidationException() {
        long userId = 1L;
        Long itemId = null;
        Item item = new Item();
        item.setOwnerId(userId);
        item.setId(itemId);
        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);

        when(mapper.toItem(itemDto)).thenReturn(item);

        assertThrows(ValidationException.class, () -> itemService.update(itemDto, userId));
    }

    @Test
    void delete() {
        long userId = 1L;
        long itemId = 1L;
        Item item = new Item();
        item.setOwnerId(userId);
        when(userJpaRepository.existsById(userId)).thenReturn(true);
        when(itemJpaRepository.findById(itemId)).thenReturn(Optional.of(item));

        itemService.delete(itemId, userId);

        verify(itemJpaRepository).deleteById(itemId);
    }

    @Test
    void delete_whenUserIdDoesNotEqualOwnerId_thenReturnNotAccessException() {
        long userId = 1L;
        long itemId = 1L;
        Item item = new Item();
        item.setOwnerId(userId + 1);
        when(userJpaRepository.existsById(userId)).thenReturn(true);
        when(itemJpaRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(NotAccessException.class, () -> itemService.delete(itemId, userId));
    }

    @Test
    void delete_whenItemDoesNotExist_thenReturnNotFoundException() {
        long userId = 1L;
        long itemId = 1L;
        Item item = new Item();
        item.setOwnerId(userId + 1);
        when(userJpaRepository.existsById(userId)).thenReturn(true);
        when(itemJpaRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.delete(itemId, userId));
    }

    @Test
    void delete_whenUserDoesNotExist_thenReturnNotFoundException() {
        long userId = 1L;
        long itemId = 1L;
        Item item = new Item();
        item.setOwnerId(userId + 1);
        when(userJpaRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemService.delete(itemId, userId));
    }

    @Test
    void searchByText() {
        String text = "text";
        List<Item> items = List.of(new Item());
        List<ItemDto> expectedItemsDto = mapper.toItemDto(items);

        when(itemJpaRepository.findAvailableByNameOrDescriptionLike(text)).thenReturn(items);
        when(mapper.toItemDto(items)).thenReturn(expectedItemsDto);

        List<ItemDto> actualItemsDto = itemService.searchByText(text);

        assertEquals(expectedItemsDto, actualItemsDto);
    }

    @Test
    void createComment() {
        long userId = 1L;
        long itemId = 2L;
        long commentId = 3L;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentId);
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setItemId(itemId);
        User user = new User();
        user.setId(userId);
        comment.setAuthor(user);
        Booking booking = new Booking();
        booking.setEnd(LocalDateTime.now().minusDays(1));


        when(commentDtoMapper.toComment(commentDto)).thenReturn(comment);
        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingJpaRepository.findPastApprovedByBookerIdAndItemId(userId, itemId))
                .thenReturn(Optional.of(booking));
        when(commentJpaRepository.save(comment)).thenReturn(comment);
        when(commentJpaRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentDtoMapper.toCommentDto(comment)).thenReturn(commentDto);

        CommentDto actualCommentDto = itemService.createComment(commentDto, userId);

        assertEquals(commentDto, actualCommentDto);
    }

    @Test
    void createComment_whenCommentHasNotBeenSaved_thenReturnNotFoundException() {
        long userId = 1L;
        long itemId = 2L;
        long commentId = 3L;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentId);
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setItemId(itemId);
        User user = new User();
        user.setId(userId);
        comment.setAuthor(user);
        Booking booking = new Booking();
        booking.setEnd(LocalDateTime.now().minusDays(1));


        when(commentDtoMapper.toComment(commentDto)).thenReturn(comment);
        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingJpaRepository.findPastApprovedByBookerIdAndItemId(userId, itemId))
                .thenReturn(Optional.of(booking));
        when(commentJpaRepository.save(comment)).thenReturn(comment);
        when(commentJpaRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.createComment(commentDto, userId));
    }

    @Test
    void createComment_whenBookingEndIsAfterNow_thenReturnNotAccessException() {
        long userId = 1L;
        long itemId = 2L;
        long commentId = 3L;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentId);
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setItemId(itemId);
        User user = new User();
        user.setId(userId);
        comment.setAuthor(user);
        Booking booking = new Booking();
        booking.setEnd(LocalDateTime.now().plusSeconds(1));


        when(commentDtoMapper.toComment(commentDto)).thenReturn(comment);
        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingJpaRepository.findPastApprovedByBookerIdAndItemId(userId, itemId))
                .thenReturn(Optional.of(booking));

        assertThrows(NotAccessException.class, () -> itemService.createComment(commentDto, userId));
    }

    @Test
    void createComment_whenUserHasNotGottenItem_thenReturnNotAccessException() {
        long userId = 1L;
        long itemId = 2L;
        long commentId = 3L;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentId);
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setItemId(itemId);
        User user = new User();
        user.setId(userId);
        comment.setAuthor(user);

        when(commentDtoMapper.toComment(commentDto)).thenReturn(comment);
        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingJpaRepository.findPastApprovedByBookerIdAndItemId(userId, itemId))
                .thenReturn(Optional.empty());

        assertThrows(NotAccessException.class, () -> itemService.createComment(commentDto, userId));
    }

    @Test
    void createComment_whenUserDoesNotExist_thenReturnNotFoundException() {
        long userId = 1L;
        CommentDto commentDto = new CommentDto();
        Comment comment = new Comment();

        when(commentDtoMapper.toComment(commentDto)).thenReturn(comment);
        when(userJpaRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.createComment(commentDto, userId));
    }
}