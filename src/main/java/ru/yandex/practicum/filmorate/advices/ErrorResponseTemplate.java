package ru.yandex.practicum.filmorate.advices;

import lombok.Data;

@Data
public class ErrorResponseTemplate {

    private String error;

    public ErrorResponseTemplate(String error) {
        this.error = error;
    }

}