package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.ReviewRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewRatingMapper implements RowMapper<ReviewRating> {

    @Override
    public ReviewRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ReviewRating.builder()
                .userId(rs.getLong("user_id"))
                .reviewId(rs.getLong("review_id"))
                .isLike(rs.getBoolean("is_like"))
                .build();
    }
}
