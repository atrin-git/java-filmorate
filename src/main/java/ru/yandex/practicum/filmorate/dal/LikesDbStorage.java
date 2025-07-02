package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.Collection;

@Slf4j
@Component("db-likes")
public class LikesDbStorage extends BaseDbStorage<Likes> implements LikesStorage {
    private static final String FIND_LIKES_ON_FILM_QUERY = "SELECT * FROM likes_on_films WHERE film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO likes_on_films (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM likes_on_films WHERE film_id = ? AND user_id = ?";

    public LikesDbStorage(JdbcTemplate jdbc, RowMapper<Likes> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Likes> getLikesOnFilm(long filmId) {
        return findMany(
                FIND_LIKES_ON_FILM_QUERY,
                filmId
        );
    }

    @Override
    public void addLikeOnFilm(Long filmId, Long userId) {
        update(
                INSERT_QUERY,
                filmId,
                userId
        );
    }

    @Override
    public void removeLikeOnFilm(Long filmId, Long userId) {
        update(
                DELETE_QUERY,
                filmId,
                userId
        );
    }
}


