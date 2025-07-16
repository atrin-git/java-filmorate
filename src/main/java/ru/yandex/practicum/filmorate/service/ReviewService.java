package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.aspects.Auditable;
import ru.yandex.practicum.filmorate.dto.mappers.ReviewMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Events;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewRatingStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service("reviewService")
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final ReviewRatingStorage reviewRatingStorage;
    @Autowired
    @Qualifier("db-films")
    private FilmStorage filmStorage;
    @Autowired
    @Qualifier("db")
    private UserStorage userStorage;

    public Review find(Long reviewId) {
        if (reviewId == null)
            throw new ValidationException("Некорректный Id отзыва");

        return reviewStorage.find(reviewId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Отзыв не найден с id: " + reviewId);
                });
    }

    @Auditable(eventName = Events.REVIEW, operationName = Operations.ADD, userId = "#review.userId", entityId = "#review.id")
    public Review create(Review review) {
        checkUserId(review.getUserId());
        checkFilmId(review.getFilmId());
        checkContent(review.getContent());
        checkIsPositive(review.getIsPositive());

        return reviewStorage.create(review);
    }

    @Auditable(eventName = Events.REVIEW, operationName = Operations.UPDATE, userId = "@reviewService.find(#updateReview.id).userId", entityId = "@reviewService.find(#updateReview.id).id", isDeleting = "true")
    public Review update(Review updateReview) {
        checkReviewId(updateReview.getId());

        Review review = find(updateReview.getId());
        review = ReviewMapper.updateReviewFields(review, updateReview);
        return reviewStorage.update(review);
    }

    @Auditable(eventName = Events.REVIEW, operationName = Operations.REMOVE, userId = "@reviewService.find(#reviewId).userId", entityId = "#reviewId", isDeleting = "true")
    public void delete(Long reviewId) {
        checkReviewId(reviewId);

        reviewStorage.delete(reviewId);
    }

    public Collection<Review> findLimitedReviews(Long filmId, Long count) {
        return reviewStorage.findLimitedReviews(filmId, count);
    }

    public void addRating(Long reviewId, Long userId, Boolean isLike) {
        checkReviewId(reviewId);
        checkUserId(userId);

        reviewRatingStorage.addRatingOnReview(reviewId, userId, isLike);
    }

    public void deleteRating(Long reviewId, Long userId) {
        checkReviewId(reviewId);
        checkUserId(userId);

        reviewRatingStorage.removeRatingOnReview(reviewId, userId);
    }

    private void checkUserId(Long userId) {
        if (userId == null)
            throw new ValidationException("Некорректный Id пользователя");

        userStorage.find(userId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Пользователь не найден с id: " + userId);
                });
    }

    private void checkFilmId(Long filmId) {
        if (filmId == null)
            throw new ValidationException("Некорректный Id фильма");

        filmStorage.find(filmId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Пользователь не найден с id: " + filmId);
                });
    }

    private void checkReviewId(Long reviewId) {
        if (reviewId == null)
            throw new ValidationException("Некорректный Id отзыва");

        reviewStorage.find(reviewId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Пользователь не найден с id: " + reviewId);
                });
    }

    private void checkContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            log.debug("Передано значение content = {}. Валидация не пройдена", content);
            throw new ValidationException("Содержание отзыва не может быть пустым");
        }
    }

    private void checkIsPositive(Boolean isPositive) {
        if (isPositive == null) {
            log.debug("Передано значение is_positive = null. Валидация не пройдена");
            throw new ValidationException("Поле isPositive обязательное");
        }
    }
}
