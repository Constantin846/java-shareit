package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum.shareit")
public class ErrorHandler {
    private static final String ERROR = "error";
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handlerNotFoundException(final NotFoundException e) {
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler({NotAccessException.class})
    //@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerNotAccessException(final NotAccessException e) {
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler({ConflictException.class})
    //@ResponseStatus(HttpStatus.CONFLICT)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerConflictException(final ConflictException e) {
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerValidationException(final RuntimeException e) {
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handlerThrowable(final Throwable e) {
        String message = INTERNAL_SERVER_ERROR;
        log.warn(message, e);
        return Map.of(ERROR, message);
    }
}