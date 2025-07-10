package ru.yandex.practicum.filmorate.dal;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

@Slf4j
@Component("db-directors")
public class DirectorDbStorage extends BaseDbStorage<Director> implements DirectorStorage {

    private static final String FIND_All_QUERY = "SELECT * FROM directors g";
    private static final String FIND_DIRECTOR_ID = "SELECT * FROM directors g WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE directors SET name = ? WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO directors(name) VALUES (?)";
    private static final String DELETE_QUERY = "DELETE FROM directors WHERE id = ?";

    public DirectorDbStorage(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    public Director create(Director director) {
        long id = this.insert(INSERT_QUERY);
        director.setId(id);
        return director;
    }

    public Collection<Director> findAll() {
        return this.findMany("SELECT * FROM directors g", new Object[0]);
    }

    public Optional<Director> find(Long id) {
        return this.findOne("SELECT * FROM directors g WHERE id = ?", new Object[]{id});
    }

    public Collection<Director> getGenresForFilm(long filmId) {
        return List.of();
    }

    public void setDirectorForFilm(long filmId, Collection<Director> genres) {
    }

    public Director update(Director result) {
        this.update("UPDATE directors SET name = ? WHERE id = ?", new Object[]{result.getName(), result.getId()});
        return result;
    }

    public void delete(Long id) {
        this.delete("DELETE FROM directors WHERE id = ?", id);
    }
}

