package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.ReviewRating;
import ru.yandex.practicum.filmorate.storage.ReviewRatingStorage;

@Slf4j
@Component
public class ReviewRatingDbStorage extends BaseDbStorage implements ReviewRatingStorage {

    public static final String INSERT_QUERY = "MERGE INTO ratings_on_reviews(user_id, review_id, isLike)" +
            "VALUES (?, ?, ?)";
    public static final String DELETE_QUERY = "DELETE FROM ratings_on_reviews " +
            "WHERE user_id = ? AND review_id = ?";

    public ReviewRatingDbStorage(JdbcTemplate jdbc, RowMapper<ReviewRating> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addRatingOnReview(Long reviewId, Long userId, Boolean isLike) {
        jdbc.update(
                INSERT_QUERY,
                userId,
                reviewId,
                isLike
        );
    }

    @Override
    public void removeRatingOnReview(Long reviewId, Long userId) {
        update(
                DELETE_QUERY,
                userId,
                reviewId
        );
    }

}
