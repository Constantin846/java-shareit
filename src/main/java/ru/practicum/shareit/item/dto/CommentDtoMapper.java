package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CommentDtoMapper {
    CommentDtoMapper MAPPER = Mappers.getMapper(CommentDtoMapper.class);

    @Mapping(target = "authorName", source = "author", qualifiedByName = "authorToAuthorName")
    CommentDto toCommentDto(Comment comment);

    Comment toComment(CommentDto commentDto);

    @Mapping(target = "authorName", source = "author", qualifiedByName = "authorToAuthorName")
    List<CommentDto> toCommentDto(List<Comment> comments);

    List<Comment> toComment(List<CommentDto> comments);

    @Mapping(target = "authorName", source = "author", qualifiedByName = "authorToAuthorName")
    Set<CommentDto> toCommentDto(Set<Comment> comments);

    @Named("authorToAuthorName")
    default String authorToAuthorName(User author) {
        return author.getName();
    }
}
