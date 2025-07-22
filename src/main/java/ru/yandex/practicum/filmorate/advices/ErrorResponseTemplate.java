package ru.yandex.practicum.filmorate.advices;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseTemplate {

    private String error;

}