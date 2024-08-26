package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.exceptions.NotAccessException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoMapper;
import ru.practicum.shareit.item.dto.ItemBookingCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentJpaRepository;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final String USER_WAS_NOT_FOUND_BY_ID = "User was not found by id: %d";
    private final ItemJpaRepository itemRepository;
    private final UserJpaRepository userRepository;
    private final CommentJpaRepository commentRepository;
    private final BookingJpaRepository bookingRepository;
    private final BookingDtoMapper bookingDtoMapper;

    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        if (userRepository.existsById(userId)) {
            Item item = ItemDtoMapper.toItem(itemDto);
            item.setOwnerId(userId);
            Item createdItem = itemRepository.save(item);
            return findById(createdItem.getId());
        } else {
            String message = String.format(USER_WAS_NOT_FOUND_BY_ID, userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public List<ItemDto> findItemsOfUser(long userId) {
        if (userRepository.existsById(userId)) {
            return itemRepository.findByOwnerId(userId).stream()
                    .map(ItemDtoMapper::toItemDto)
                    .toList();
        } else {
            String message = String.format(USER_WAS_NOT_FOUND_BY_ID, userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public ItemDto findById(long itemId) {
        Item item = getItemById(itemId);
        return ItemDtoMapper.toItemDto(item);
    }

    @Override
    public ItemBookingCommentDto findItemBookingCommentsById(long itemId) {
        Item item = getItemById(itemId);
        ItemBookingCommentDto itemBCDto = new ItemBookingCommentDto(ItemDtoMapper.toItemDto(item));

        return setComments(itemBCDto);
    }

    @Override
    public ItemDto update(ItemDto itemDto, long userId) {
        Item item = ItemDtoMapper.toItem(itemDto);

        if (item.getId() == null) {
            String message = String.format("The item's id is null: %s", item);
            log.warn(message);
            throw new ValidationException(message);
        }

        Item oldItem = getItemById(item.getId());
        if (oldItem.getOwnerId() != userId) {
            String message = String.format("Not access to item: %s", item);
            log.warn(message);
            throw new NotFoundException(message);
        }

        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        oldItem.setAvailable(item.isAvailable());

        if (userRepository.existsById(userId)) {
            Item newItem = itemRepository.save(oldItem);
            return findById(newItem.getId());
        } else {
            String message = String.format(USER_WAS_NOT_FOUND_BY_ID, userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public void delete(long itemId, long userId) {
        if (userRepository.existsById(userId)) {
            if (!checkUserAccess(itemId, userId)) {
                String message = String.format("Not access to item: %s", itemId);
                log.warn(message);
                throw new NotAccessException(message);
            }
            itemRepository.deleteById(itemId);
        } else {
            String message = String.format(USER_WAS_NOT_FOUND_BY_ID, userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public List<ItemDto> searchByText(String text) {
        List<Item> items = itemRepository.findAvailableByNameOrDescriptionLike(text);
        return items.stream()
                .map(ItemDtoMapper::toItemDto)
                .toList();
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, long userId) {
        Comment comment = CommentDtoMapper.toComment(commentDto);
        Optional<User> userOp = userRepository.findById(userId);
        User user = userOp.orElseThrow(() -> {
            String message = String.format("User was not found by id: %d", userId);
            log.warn(message);
            return new NotFoundException(message);
        });
        comment.setAuthor(user);

        Optional<Booking> bookingOp = bookingRepository.findPastApprovedByBookerIdAndItemId(
                comment.getAuthor().getId(), comment.getItemId());
        Booking booking = bookingOp.orElseThrow(() -> {
            String message = String.format("User=%d has not gotten this item=%d",
                    comment.getAuthor().getId(), comment.getItemId());
            log.warn(message);
            return new NotAccessException(message);
        });
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();

        System.out.println("\n" + start + "\n" + now + "\n" + end + "\n");

        if (!booking.getEnd().isBefore(LocalDateTime.now())) {
            String message = String.format("User=%d has not returned this item=%d",
                    comment.getAuthor().getId(), comment.getItemId());
            log.warn(message);
            throw  new NotAccessException(message);
        }
        Comment savedComment = commentRepository.save(comment);

        Optional<Comment> commentOp = commentRepository.findById(savedComment.getId());
        savedComment = commentOp.orElseThrow(() -> {
            String message = String.format("Comment has not been saved: %s", comment);
            log.warn(message);
            return new NotFoundException(message);
        });
        CommentDto newCommentDto = CommentDtoMapper.toCommentDto(savedComment);
        newCommentDto.setCreated(true);
        return newCommentDto;
    }

    private boolean checkUserAccess(long itemId, long userId) {
        Optional<Item> itemOp = itemRepository.findById(itemId);
        Item oldItem = itemOp.orElseThrow(() -> {
            String message = String.format("Item does not exist with id: %d", itemId);
            log.warn(message);
            return new NotFoundException(message);
        });
        return oldItem.getOwnerId() == userId;
    }

    private Item getItemById(long itemId) {
        Optional<Item> itemOp = itemRepository.findById(itemId);
        return itemOp.orElseThrow(() -> {
            String message = String.format("Item was not found by id: %d", itemId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }

    private ItemBookingCommentDto setBookings(ItemBookingCommentDto itemBCDto) {
        LinkedList<Booking> bookings = bookingRepository.findApprovedByItemIdSortStartAsc(itemBCDto.getId());
        LocalDateTime now = LocalDateTime.now();

        if (bookings.size() == 1) {
            Booking booking = bookings.getFirst();
            if (booking.getStart().isBefore(now)) {
                itemBCDto.setLastBooking(bookingDtoMapper.toBookingDto(booking));
            }
        } else if (bookings.size() > 1) {
            for (int i = 1; i < bookings.size(); i++) {
                if (bookings.get(i).getStart().isAfter(now)) {
                    itemBCDto.setNextBooking(bookingDtoMapper.toBookingDto(bookings.get(i)));
                    itemBCDto.setLastBooking(bookingDtoMapper.toBookingDto(bookings.get(i - 1)));
                }
            }
        }
        return itemBCDto;
    }

    private ItemBookingCommentDto setComments(ItemBookingCommentDto itemBCDto) {
        Set<Comment> comments = commentRepository.findByItemId(itemBCDto.getId());
        Set<CommentDto> commentDtos = comments.stream()
                        .map(CommentDtoMapper::toCommentDto)
                        .collect(Collectors.toSet());
        itemBCDto.setComments(commentDtos);
        return itemBCDto;
    }
}
