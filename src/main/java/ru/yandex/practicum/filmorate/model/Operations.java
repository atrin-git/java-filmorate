package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Operations {
    REMOVE(1),
    ADD(2),
    UPDATE(3);

    private final Integer operationId;
}
