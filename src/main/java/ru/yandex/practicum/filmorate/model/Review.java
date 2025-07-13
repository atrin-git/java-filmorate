package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {

    private Long id;
    private Long filmId;
    private Long userId;
    private Boolean isPositive;
    private String content;
    private Long useful;

}
