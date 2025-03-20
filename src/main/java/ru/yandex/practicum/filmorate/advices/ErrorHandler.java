package ru.yandex.practicum.filmorate.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseTemplate handleDuplicateException(final DuplicateException e) {
        return new ErrorResponseTemplate(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseTemplate handleValidationException(final ValidationException e) {
        return new ErrorResponseTemplate(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseTemplate handleNotFoundException(final NotFoundException e) {
        return new ErrorResponseTemplate(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseTemplate handleIllegalArgumentException(final IllegalArgumentException e) {
        return new ErrorResponseTemplate(e.getMessage());
    }
}
