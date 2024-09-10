package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.User;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CommentDtoMapperImplTest {
    @InjectMocks
    private CommentDtoMapper mapper = CommentDtoMapper.MAPPER;
    private Comment comment;
    private CommentDto commentDto;

    @Test
    void toCommentDto() {
        createComments();

        CommentDto actualCommentDto = mapper.toCommentDto(comment);

        assertEquals(commentDto.getId(), actualCommentDto.getId());
    }

    @Test
    void toCommentDto_whenCommentIsNull_thenReturnNull() {
        comment = null;

        CommentDto actualCommentDto = mapper.toCommentDto(comment);

        assertNull(actualCommentDto);
    }

    @Test
    void toComment() {
        createComments();

        Comment actualComment = mapper.toComment(commentDto);

        assertEquals(comment.getId(), actualComment.getId());
    }

    @Test
    void toComment_whenCommentDtoIsNull_thenReturnNull() {
        commentDto = null;

        Comment actualComment = mapper.toComment(commentDto);

        assertNull(actualComment);
    }

    @Test
    void toCommentDto_setComments() {
        createComments();
        Set<Comment> comments = Set.of(comment);
        Set<CommentDto> commentsDto = Set.of(commentDto);

        Set<CommentDto> actualCommentsDto = mapper.toCommentDto(comments);

        assertArrayEquals(commentsDto.toArray(), actualCommentsDto.toArray());
    }

    @Test
    void toCommentDto_setComments_whenSetCommentIsNull_thenReturnNull() {
        Set<Comment> comments = null;

        Set<CommentDto> actualCommentsDto = mapper.toCommentDto(comments);

        assertNull(actualCommentsDto);
    }

    private void createComments() {
        long id = 1L;
        String authorName = "author name";
        comment = new Comment();
        comment.setId(id);
        User user = new User();
        user.setName(authorName);
        comment.setAuthor(user);
        commentDto = new CommentDto();
        commentDto.setId(id);
        commentDto.setAuthorName(authorName);
    }
}