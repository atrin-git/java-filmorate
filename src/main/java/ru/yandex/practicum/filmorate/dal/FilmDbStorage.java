package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenresStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.RatesStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Component("db-films")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    @Autowired
    @Qualifier("db-genres")
    private GenresStorage genresStorage;
    @Autowired
    @Qualifier("db-likes")
    private LikesStorage likeStorage;
    @Autowired
    @Qualifier("db-rates")
    private RatesStorage ratesStorage;

    private static final String FIND_ALL_FILMS_QUERY = "SELECT * FROM films";
    private static final String FIND_FILM_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, rating_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET " +
            "name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String DELETE_ALL_QUERY = "DELETE FROM films";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film create(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating().getId()
        );

        film.setId(id);

        if (film.getGenres() != null) {
            genresStorage.setGenresForFilm(id, film.getGenres());
        }

        ratesStorage.getFilmRating(id).ifPresent(film::setRating);

        return film;
    }

    @Override
    public void delete(Long id) {
        delete(
                DELETE_QUERY,
                id
        );
    }

    @Override
    public void deleteAll() {
        deleteAll(
                DELETE_ALL_QUERY
        );
    }

    @Override
    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating().getId(),
                film.getId()
        );
        return film;
    }

    @Override
    public Optional<Film> find(Long id) {
        Optional<Film> film = findOne(
                FIND_FILM_QUERY,
                id
        );

        film.ifPresent(value -> {
            value.setGenres(
                    genresStorage.getGenresForFilm(value.getId()).stream()
                            .collect(Collectors.toSet())
            );
            value.setLikesByUsers(
                    likeStorage.getLikesOnFilm(value.getId()).stream()
                            .map(Likes::getUserId)
                            .collect(Collectors.toSet())
            );
            ratesStorage.getFilmRating(value.getId()).ifPresent(value::setRating);
        });

        return film;
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> films = findMany(
                FIND_ALL_FILMS_QUERY
        );

        films.forEach(film -> {
            film.setGenres(
                    genresStorage.getGenresForFilm(film.getId()).stream()
                            .collect(Collectors.toSet())
            );
            film.setLikesByUsers(
                    likeStorage.getLikesOnFilm(film.getId()).stream()
                            .map(Likes::getUserId)
                            .collect(Collectors.toSet())
            );
            ratesStorage.getFilmRating(film.getId()).ifPresent(film::setRating);
        });

        return films;
    }
}
