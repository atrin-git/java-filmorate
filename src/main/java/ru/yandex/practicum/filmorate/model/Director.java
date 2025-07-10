package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;

@Data
@Builder(toBuilder = true)
public class Director {
    private Long id;
    private String name;
}
