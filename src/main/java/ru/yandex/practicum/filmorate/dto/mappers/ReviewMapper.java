package ru.yandex.practicum.filmorate.dto.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;

@Slf4j
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

    public static void checkContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            log.debug("Передано значение content = {}. Валидация не пройдена", content);
            throw new ValidationException("Содержание отзыва не может быть пустым");
        }
    }

    public static void checkIsPositive(Boolean isPositive) {
        if (isPositive == null) {
            log.debug("Передано значение is_positive = null. Валидация не пройдена");
            throw new ValidationException("Поле isPositive обязательное");
        }
    }

}
