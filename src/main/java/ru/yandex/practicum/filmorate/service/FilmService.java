package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.validation.FilmValidator;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmValidator filmValidator;

    public Collection<Film> findAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        filmValidator.checkFilmIsValid(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        filmValidator.checkFilmIsValid(film);
        return filmStorage.update(film);
    }

    public void deleteAll() {
        filmStorage.deleteAll();
    }

    public void addLike(Long filmId, Long userId) {
        final Film film = filmStorage.find(filmId);
        if (film == null) {
            return;
        }
        final User user = userStorage.find(userId);
        if (user == null) {
            return;
        }

        Set<Long> likes = film.getLikesByUsers();
        likes.add(userId);
        film.setLikesByUsers(likes);
        filmStorage.update(film);
    }

    public void deleteLike(Long filmId, Long userId) {
        final Film film = filmStorage.find(filmId);
        if (film == null) {
            return;
        }
        final User user = userStorage.find(userId);
        if (user == null) {
            return;
        }

        Set<Long> likes = film.getLikesByUsers();
        likes.remove(userId);
        film.setLikesByUsers(likes);
        filmStorage.update(film);
    }

    public Collection<Film> getPopularFilms(int count) {
        Collection<Film> films = filmStorage.getAll();
        return films.stream()
                .sorted((f1, f2) -> f2.getLikesByUsers().size() - f1.getLikesByUsers().size())
                .limit(count)
                .toList();
    }
}
