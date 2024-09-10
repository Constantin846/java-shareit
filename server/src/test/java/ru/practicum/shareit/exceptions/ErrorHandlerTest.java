package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {
    @InjectMocks
    private ErrorHandler errorHandler;

    @Test
    void handlerNotAccessException() {
        String expectedMessage = "Not access exception";
        NotAccessException e = new NotAccessException(expectedMessage);

        Map<String, String> response = errorHandler.handlerNotAccessException(e);

        assertTrue(response.containsValue(expectedMessage));
    }

    @Test
    void handlerConflictException() {
        String expectedMessage = "Conflict exception";
        ConflictException e = new ConflictException(expectedMessage);

        Map<String, String> response = errorHandler.handlerConflictException(e);

        assertTrue(response.containsValue(expectedMessage));
    }

    @Test
    void handlerValidationException() {
        String expectedMessage = "Validation exception";
        ValidationException e = new ValidationException(expectedMessage);

        Map<String, String> response = errorHandler.handlerValidationException(e);

        assertTrue(response.containsValue(expectedMessage));
    }
}