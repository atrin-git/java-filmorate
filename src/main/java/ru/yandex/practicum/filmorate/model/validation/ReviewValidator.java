package ru.yandex.practicum.filmorate.model.validation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;

@Slf4j
@Component
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewValidator {
    public static void checkReviewIsValid(Review review) {
        checkUserIdIsValid(review.getUserId());
        checkFilmIdIsValid(review.getFilmId());
        checkContentIsValid(review.getContent());
        checkIsPositiveIsValid(review.getIsPositive());
    }

    public static void checkUserIdIsValid(Long userId) {
        if (userId == null) {
            log.debug("Передано значение user_id = {}. Валидация не пройдена", userId);
            throw new ValidationException("Идентификатор пользователя должен быть больше или равен 1");
        }
    }

    public static void checkFilmIdIsValid(Long filmId) {
        if (filmId == null) {
            log.debug("Передано значение film_id = {}. Валидация не пройдена", filmId);
            throw new ValidationException("Идентификатор фильма должен быть больше или равен 1");
        }
    }

    public static void checkContentIsValid(String content) {
        if (content == null || content.trim().isEmpty()) {
            log.debug("Передано значение content = {}. Валидация не пройдена", content);
            throw new ValidationException("Содержание отзыва не может быть пустым");
        }
    }

    public static void checkIsPositiveIsValid(Boolean isPositive) {
        if (isPositive == null) {
            log.debug("Передано значение is_positive = null. Валидация не пройдена");
            throw new ValidationException("Поле isPositive обязательное");
        }
    }

    public static void checkReviewIdIsValid(Long reviewId) {
        if (reviewId == null) {
            log.debug("Передано значение review_id = {}. Валидация не пройдена", reviewId);
            throw new ValidationException("Идентификатор отзыва должен быть больше или равен 1");
        }
    }
}
