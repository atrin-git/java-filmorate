package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenresStorage {
    Collection<Genre> findAll();

    Optional<Genre> find(Long id);

    Collection<Genre> getGenresForFilm(long filmId);

    void setGenresForFilm(long filmId, Collection<Genre> genres);

}
