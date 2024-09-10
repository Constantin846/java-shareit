package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.User;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentDtoMapperTest {
    @Mock
    User author;
    @InjectMocks
    private CommentDtoMapper mapper = CommentDtoMapper.MAPPER;

    @Test
    void authorToAuthorName() {
        mapper.authorToAuthorName(author);

        verify(author).getName();
    }
}