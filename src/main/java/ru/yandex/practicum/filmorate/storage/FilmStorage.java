package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    void delete(Long id);

    Film update(Film film);

    Film find(Long id);

    Collection<Film> getAll();
}
