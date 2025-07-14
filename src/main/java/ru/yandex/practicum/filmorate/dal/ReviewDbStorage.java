package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component("db-reviews")
public class ReviewDbStorage extends BaseDbStorage<Review> implements ReviewStorage {

    private static final String FIND_REVIEW_QUERY = """
            SELECT r.id, r.film_id, r.user_id, r.isPositive, r.content,
            COALESCE(SUM(
            CASE
            WHEN rr.isLike = TRUE THEN 1
            WHEN rr.isLike = FALSE THEN -1
            ELSE 0
            END
            ), 0) AS useful
            FROM reviews AS r 
            LEFT JOIN ratings_on_reviews AS rr
            ON rr.review_id = r.id
            WHERE id = ?
            GROUP BY r.id, r.film_id, r.user_id, r.isPositive, r.content
            """;
    private static final String INSERT_QUERY = "INSERT INTO reviews(film_id, user_id, isPositive, content)" +
            "VALUES(?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE reviews SET " +
            "film_id = ?, user_id = ?, isPositive = ?, content = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM reviews WHERE id = ?";
    private static final String FIND_LIMITED_REVIEWS_QUERY = """
            SELECT r.id, r.film_id, r.user_id, r.isPositive, r.content, 
            COALESCE(SUM(
            CASE
            WHEN rr.isLike = TRUE THEN 1
            WHEN rr.isLike = FALSE THEN -1
            ELSE 0
            END
            ), 0) AS useful
            FROM reviews AS r
            LEFT JOIN ratings_on_reviews AS rr
            ON rr.review_id = r.id
            GROUP BY r.id, r.film_id, r.user_id, r.isPositive, r.content
            ORDER BY useful DESC
            LIMIT ?
            """;
    private static final String FIND_LIMITED_REVIEWS_BY_FILM_ID_QUERY = """
            SELECT r.id, r.film_id, r.user_id, r.isPositive, r.content, 
            COALESCE(SUM(
            CASE
            WHEN rr.isLike = TRUE THEN 1
            WHEN rr.isLike = FALSE THEN -1
            ELSE 0
            END
            ), 0) AS useful 
            FROM reviews AS r
            LEFT JOIN ratings_on_reviews AS rr
            ON rr.review_id = r.id
            WHERE film_id = ?
            GROUP BY r.id, r.film_id, r.user_id, r.isPositive, r.content
            ORDER BY useful DESC
            LIMIT ?
            """;

    public ReviewDbStorage(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Review> find(Long reviewId) {
        return findOne(FIND_REVIEW_QUERY, reviewId);
    }

    @Override
    public Review create(Review review) {
        Long id = insert(
                INSERT_QUERY,
                review.getFilmId(),
                review.getUserId(),
                review.getIsPositive(),
                review.getContent()
        );

        review.setId(id);
        return review;
    }

    @Override
    public Review update(Review review) {
        update(
                UPDATE_QUERY,
                review.getFilmId(),
                review.getUserId(),
                review.getIsPositive(),
                review.getContent(),
                review.getId()
        );

        return review;
    }

    @Override
    public void delete(Long reviewId) {
        delete(
                DELETE_QUERY,
                reviewId
        );
    }

    @Override
    public Collection<Review> findLimitedReviews(Long filmId, Long count) {
        if (filmId == null)
            return findMany(FIND_LIMITED_REVIEWS_QUERY, count);
        else
            return findMany(FIND_LIMITED_REVIEWS_BY_FILM_ID_QUERY, filmId, count);
    }
}
