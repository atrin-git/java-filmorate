package ru.yandex.practicum.filmorate.model.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;

@Slf4j
@Component
public class ReviewValidator {

    public void checkReviewIsValid(Review review) {

        if (review.getUserId() == null || review.getUserId() < 1) {
            log.debug("Передано значение user_id = {}. Валидация не пройдена", review.getUserId());
            throw new ValidationException("Идентификатор пользователя должен быть больше или равен 1");
        }
        if (review.getFilmId() == null || review.getFilmId() < 1) {
            log.debug("Передано значение film_id = {}. Валидация не пройдена", review.getFilmId());
            throw new ValidationException("Идентификатор фильма должен быть больше или равен 1");
        }
        if (review.getContent() == null || review.getContent().trim().isEmpty()) {
            log.debug("Передано значение content = {}. Валидация не пройдена", review.getContent());
            throw new ValidationException("Содержание отзыва не может быть пустым");
        }
        if (review.getIsPositive() == null) {
            log.debug("Передано значение is_positive = null. Валидация не пройдена");
            throw new ValidationException("Поле isPositive обязательное");
        }
    }

}
