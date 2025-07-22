package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.aspects.Auditable;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.dto.mappers.ReviewMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Events;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewRatingStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.model.validation.ReviewValidator.*;

@Slf4j
@Service("reviewService")
public class ReviewService {

    @Autowired
    private ReviewStorage reviewStorage;
    @Autowired
    private ReviewRatingStorage reviewRatingStorage;
    @Autowired
    private FilmDbStorage filmStorage;
    @Autowired
    private UserDbStorage userStorage;

    public Review find(Long reviewId) {
        if (reviewId == null) {
            throw new ValidationException("Некорректный Id отзыва");
        }

        return reviewStorage.find(reviewId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Отзыв не найден с id: " + reviewId);
                });
    }

    @Auditable(eventName = Events.REVIEW, operationName = Operations.ADD, userId = "#review.userId", entityId = "#review.id")
    public Review create(Review review) {
        checkReviewIsValid(review);
        userStorage.find(review.getUserId()).orElseThrow(() ->
                new NotFoundException("Не найден пользователь с id = " + review.getUserId()));
        filmStorage.find(review.getFilmId()).orElseThrow(() ->
                new NotFoundException("Не найден фильм с id = " + review.getFilmId()));

        return reviewStorage.create(review);
    }

    @Auditable(eventName = Events.REVIEW, operationName = Operations.UPDATE, userId = "@reviewService.find(#updateReview.id).userId", entityId = "@reviewService.find(#updateReview.id).id", isDeleting = "true")
    public Review update(Review updateReview) {
        checkReviewIdIsValid(updateReview.getId());

        Review review = find(updateReview.getId());
        review = ReviewMapper.updateReviewFields(review, updateReview);
        return reviewStorage.update(review);
    }

    @Auditable(eventName = Events.REVIEW, operationName = Operations.REMOVE, userId = "@reviewService.find(#reviewId).userId", entityId = "#reviewId", isDeleting = "true")
    public void delete(Long reviewId) {
        checkReviewIdIsValid(reviewId);
        find(reviewId);

        reviewStorage.delete(reviewId);
    }

    public Collection<Review> findLimitedReviews(Long filmId, Long count) {
        return reviewStorage.findLimitedReviews(filmId, count);
    }

    public void addRating(Long reviewId, Long userId, Boolean isLike) {
        checkReviewIdIsValid(reviewId);
        checkUserIdIsValid(userId);

        find(reviewId);
        userStorage.find(userId).orElseThrow(() ->
                new NotFoundException("Не найден пользователь с id = " + userId));

        reviewRatingStorage.addRatingOnReview(reviewId, userId, isLike);
    }

    public void deleteRating(Long reviewId, Long userId) {
        checkReviewIdIsValid(reviewId);
        checkUserIdIsValid(userId);

        find(reviewId);
        userStorage.find(userId).orElseThrow(() ->
                new NotFoundException("Не найден пользователь с id = " + userId));

        reviewRatingStorage.removeRatingOnReview(reviewId, userId);
    }

}
