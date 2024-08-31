package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;

public class CommentDtoMapper {
    private CommentDtoMapper() {
    }

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setItemId(comment.getItemId());
        commentDto.setAuthorName(comment.getAuthor().getName());
        return commentDto;
    }

    public static Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setItemId(commentDto.getItemId());
        return comment;
    }
}
