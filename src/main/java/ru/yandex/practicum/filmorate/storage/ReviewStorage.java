package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {

    Optional<Review> find(Long reviewId);

    Review create(Review review);

    Review update(Review review);

    void delete(Long reviewId);

    Collection<Review> findLimitedReviews(Long filmId, Long count);

}
