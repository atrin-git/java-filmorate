package ru.yandex.practicum.filmorate.dal;

import java.util.Collection;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DirectorsFilmsStorage;

@Component("db-directorsFilms")
public class DirectorsFilmsDbStorage implements DirectorsFilmsStorage {
    private final RowMapper<Film> filmRowMapper;
    private final RowMapper<Director> directorRowMapper;
    private final JdbcTemplate jdbc;
    private static final String INSERT_DIRECTORS_FILMS_QUERY = "INSERT INTO directors_on_films (director_id, film_id) VALUES (?, ?)";
    private static final String FIND_FILMS_DIRECTORS_QUERY = """
    SELECT f.*
    FROM Films AS f
    JOIN directors_on_films AS dof ON f.id = dof.film_id
    WHERE dof.director_id = ?
    ORDER BY f.release_date
    """;
    private static final String FIND_DIRECTOR_FILM_QUERY = """
    SELECT d.*
    FROM directors AS d
    JOIN directors_on_films AS dof ON d.id = dof.director_id
    WHERE dof.film_id = ?
    """;

    public DirectorsFilmsDbStorage(JdbcTemplate jdbc, RowMapper<Director> directorRowMapper, RowMapper<Film> filmRowMapper) {
        this.filmRowMapper = filmRowMapper;
        this.directorRowMapper = directorRowMapper;
        this.jdbc = jdbc;
    }

    //добавить режиссеров в фильм
    public void setDirectorsForFilm(long id, Set<Director> directors) {
        directors.forEach((dir) -> {
            jdbc.update(INSERT_DIRECTORS_FILMS_QUERY, dir.getId(), id);
        });
    }

    //возврат фильмов режиссера
    public Collection<Film> getDirectorOnFilm(Long id) {
        return jdbc.query(FIND_FILMS_DIRECTORS_QUERY, filmRowMapper, id);
    }

    //возврат режиссеров фильма
    @Override
    public Collection<Director> getDirectorsForFilm(Long id) {
        return jdbc.query(FIND_DIRECTOR_FILM_QUERY, directorRowMapper, id);
    }
}

