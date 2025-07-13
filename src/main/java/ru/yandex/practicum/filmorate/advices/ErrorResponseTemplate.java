package ru.yandex.practicum.filmorate.advices;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponseTemplate {

    private String error;

    public ErrorResponseTemplate(String error) {
        this.error = error;
    }

}