package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.Director;

public interface DirectorStorage {
    Director create(Director director);

    Collection<Director> findAll();

    Optional<Director> find(Long id);

    Collection<Director> getGenresForFilm(long filmId);

    void setDirectorForFilm(long filmId, Collection<Director> directors);

    Director update(Director result);

    void delete(Long id);
}

