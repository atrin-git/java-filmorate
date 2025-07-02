package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresStorage;

import java.util.Collection;
import java.util.Optional;


@Slf4j
@Component("db-genres")
public class GenresDbStorage extends BaseDbStorage<Genre> implements GenresStorage {
    private static final String FIND_All_QUERY = "SELECT * FROM genres g";
    private static final String FIND_QUERY = "SELECT * FROM genres g WHERE id = ?";
    private static final String FIND_FILM_GENRES_QUERY = "SELECT * FROM genres g " +
            "WHERE id IN (SELECT genre_id FROM genres_on_films gof WHERE gof.film_id = ?)";
    private static final String INSERT_GENRES_QUERY = "INSERT INTO genres_on_films (genre_id, film_id) VALUES (?, ?)";

    public GenresDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Genre> findAll() {
        return findMany(FIND_All_QUERY);
    }

    @Override
    public Optional<Genre> find(Long id) {
        return findOne(
                FIND_QUERY,
                id
        );
    }

    @Override
    public Collection<Genre> getGenresForFilm(long filmId) {
        return findMany(
                FIND_FILM_GENRES_QUERY,
                filmId
        );
    }

    @Override
    public void setGenresForFilm(long filmId, Collection<Genre> genres) {
        genres.forEach(genre -> {
            update(
                    INSERT_GENRES_QUERY,
                    genre.getId(),
                    filmId
            );
        });
    }
}
