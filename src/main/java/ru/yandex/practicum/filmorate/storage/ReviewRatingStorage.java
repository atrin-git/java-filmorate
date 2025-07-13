package ru.yandex.practicum.filmorate.storage;

public interface ReviewRatingStorage {

    void addRatingOnReview(Long reviewId, Long userId, Boolean isLike);
    void removeRatingOnReview(Long reviewId, Long userId);

}
