package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review create(@RequestBody Review review) {
        log.info("Получен запрос на добавление отзыва");
        return reviewService.create(review);
    }

    @PutMapping
    public Review update(@RequestBody Review review) {
        log.info("Получен запрос на обновление отзыва с id = {}", review.getId());
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        log.info("Получен запрос на удаление отзыва с id = {}", id);
        reviewService.delete(id);
    }

    @GetMapping("/{id}")
    public Review find(@PathVariable("id") Long id) {
        return reviewService.find(id);
    }

    @GetMapping
    public Collection<Review> findLimitedReviews(
            @RequestParam(name = "filmId", required = false) Long filmId,
            @RequestParam(name = "count", defaultValue = "10") Long count
    ) {
        return reviewService.findLimitedReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long reviewId, @PathVariable("userId") Long userId) {
        log.info("Получен запрос на добавление лайка на отзыв с id: {}, пользователем с id: {}", reviewId, userId);
        reviewService.addRating(reviewId, userId, true);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long reviewId, @PathVariable("userId") Long userId) {
        log.info("Получен запрос на удаление лайка на отзыве с id: {}, пользователем с id: {}", reviewId, userId);
        reviewService.deleteRating(reviewId, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable("id")  Long reviewId, @PathVariable("userId") Long userId) {
        log.info("Получен запрос на добавление дизлайка на отзыв с id: {}, пользователем с id: {}", reviewId, userId);
        reviewService.addRating(reviewId, userId, false);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable("id") Long reviewId, @PathVariable("userId") Long userId) {
        log.info("Получен запрос на удаление дизлайка на отзыве с id: {}, пользователем с id: {}", reviewId, userId);
        reviewService.deleteRating(reviewId, userId);
    }

}
