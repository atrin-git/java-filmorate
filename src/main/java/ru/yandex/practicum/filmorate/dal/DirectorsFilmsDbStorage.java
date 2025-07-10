package ru.yandex.practicum.filmorate.dal;

import java.util.Collection;
import java.util.Set;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorsFilmsStorage;

@Component("db-directorsFilms")
public class DirectorsFilmsDbStorage extends BaseDbStorage<Director> implements DirectorsFilmsStorage {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(DirectorsFilmsDbStorage.class);
    private static final String INSERT_GENRES_QUERY = "INSERT INTO directors_on_films (director_id, film_id) VALUES (?, ?)";
    private static final String FIND_FILM_GENRES_QUERY = "SELECT * FROM directors g WHERE id IN (SELECT director_id FROM directors_on_films gof WHERE gof.film_id = ?)";

    public DirectorsFilmsDbStorage(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    public void setDirectorsForFilm(long id, Set<Director> directors) {
        directors.forEach((dir) -> {
            this.update("INSERT INTO directors_on_films (director_id, film_id) VALUES (?, ?)", new Object[]{dir.getId(), id});
        });
    }

    public Collection<Director> getDirectorOnFilm(Long id) {
        return this.findMany("SELECT * FROM directors g WHERE id IN (SELECT director_id FROM directors_on_films gof WHERE gof.film_id = ?)", new Object[]{id});
    }
}

