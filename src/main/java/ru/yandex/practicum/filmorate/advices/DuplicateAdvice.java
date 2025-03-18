package ru.yandex.practicum.filmorate.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.DuplicateException;

@ControllerAdvice
public class DuplicateAdvice {
    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponseTemplate handleException(DuplicateException e) {
        return new ErrorResponseTemplate(e.getMessage());
    }

}
