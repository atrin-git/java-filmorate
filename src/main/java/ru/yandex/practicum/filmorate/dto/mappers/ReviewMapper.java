package ru.yandex.practicum.filmorate.dto.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Review;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewMapper {
    public static Review updateReviewFields(Review review, Review request) {
        if (request.hasIsPositive()) {
            review.setIsPositive(request.getIsPositive());
        }
        if (request.hasContent()) {
            review.setContent(request.getContent());
        }
        if (request.hasUseful()) {
            review.setUseful(request.getUseful());
        }
        return review;
    }
}
