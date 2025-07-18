package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewRating {

    private Long userId;
    private Long reviewId;
    private Boolean isLike;

}
