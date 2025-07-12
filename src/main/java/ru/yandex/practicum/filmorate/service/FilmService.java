package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.DirectorsFilmsDbStorage;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Events;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.validation.FilmValidator;
import ru.yandex.practicum.filmorate.storage.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@Service
public class FilmService {
    @Autowired
    @Qualifier("db-films")
    private FilmStorage filmStorage;
    @Autowired
    @Qualifier("db")
    private UserStorage userStorage;
    @Autowired
    @Qualifier("db-genres")
    private GenresStorage genresStorage;
    @Autowired
    @Qualifier("db-likes")
    private LikesStorage likeStorage;
    @Autowired
    @Qualifier("db-rates")
    private RatesStorage ratesStorage;
    @Autowired
    @Qualifier("db-directorsFilms")
    private DirectorsFilmsDbStorage directorsFilmsDbStorage;
    @Autowired
    @Qualifier("db-audit")
    private AuditStorage auditStorage;
    @Autowired
    private FilmValidator filmValidator;

    public Collection<FilmDto> findAll() {
        return filmStorage.getAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public FilmDto find(Long filmId) {
        if (filmId == null || filmId < 1)
            throw new ValidationException("Указан некорректный id пользователя");
        return filmStorage.find(filmId)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> {
                    log.warn("Не найден фильм с ID = {}", filmId);
                    return new NotFoundException("Фильм не найден с ID: " + filmId);
                });

    }

    public FilmDto create(NewFilmRequest newFilm) {
        Film film = FilmMapper.mapToFilm(newFilm);
        filmValidator.checkFilmIsValid(film);

        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> {
                genresStorage.find(genre.getId())
                        .orElseThrow(() -> {
                            log.warn("Не найден жанр с ID = {}", genre.getId());
                            return new NotFoundException("Не найден жанр с ID: " + genre.getId());
                        });
            });
        }

        Long rateId = film.getRating().getId();
        if (rateId != null) {
            ratesStorage.find(rateId)
                    .orElseThrow(() -> {
                        log.warn("Не найден рейтинг с ID = {}", rateId);
                        return new NotFoundException("Не найден рейтинг с ID: " + rateId);
                    });
        }

        film = filmStorage.create(film);

        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto update(UpdateFilmRequest updateFilm) {
        Film film = FilmMapper.mapToFilm(updateFilm);
        filmValidator.checkFilmIsValid(film);

        Film oldFilm = filmStorage.find(updateFilm.getId())
                .orElseThrow(() -> {
                    log.warn("Не найден фильм с ID = {}", updateFilm.getId());
                    return new NotFoundException("Фильм не найден с ID: " + updateFilm.getId());
                });

        film = FilmMapper.updateFilmFields(oldFilm, updateFilm);
        return FilmMapper.mapToFilmDto(filmStorage.update(film));
    }

    public void delete(Long filmId) {
        if (filmId == null || filmId < 1)
            throw new ValidationException("Указан некорректный id пользователя");
        filmStorage.find(filmId)
                        .orElseThrow(() -> {
                            log.warn("Не найден фильм с ID = {}", filmId);
                            return new NotFoundException("Фильм не найден с ID: "  + filmId);
                        });

        filmStorage.delete(filmId);
    }

    public void addLike(Long filmId, Long userId) {
        final Film film = filmStorage.find(filmId)
                .orElseThrow(() -> {
                    log.warn("Не найден фильм с ID = {}", filmId);
                    return new NotFoundException("Не найден фильм с ID: " + filmId);
                });

        final User user = userStorage.find(userId)
                .orElseThrow(() -> {
                    log.warn("Не найден пользователь с ID = {}", userId);
                    return new NotFoundException("Не найден пользователь с ID: " + userId);
                });

        Set<Long> likes = film.getLikesByUsers();
        likes.add(userId);
        film.setLikesByUsers(likes);
        likeStorage.addLikeOnFilm(filmId, userId);
        auditStorage.addEvent(userId, Events.LIKE, Operations.ADD, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        final Film film = filmStorage.find(filmId)
                .orElseThrow(() -> {
                    log.warn("Не найден фильм с ID = {}", filmId);
                    return new NotFoundException("Не найден фильм с ID: " + filmId);
                });

        final User user = userStorage.find(userId)
                .orElseThrow(() -> {
                    log.warn("Не найден пользователь с ID = {}", userId);
                    return new NotFoundException("Не найден пользователь с ID: " + userId);
                });

        Set<Long> likes = film.getLikesByUsers();
        likes.remove(userId);
        film.setLikesByUsers(likes);
        likeStorage.removeLikeOnFilm(filmId, userId);
        auditStorage.addEvent(userId, Events.LIKE, Operations.REMOVE, filmId);
    }

    public Collection<FilmDto> getPopularFilms(Long count, Long genreId, Long year) {
        Collection<Film> films = filmStorage.getAll();
        Stream<Film> filmStream = films.stream();
        if (genreId != null) {
            filmStream = filmStream.filter(film -> film.getGenres().stream()
                    .anyMatch(genre -> Objects.equals(genre.getId(), genreId)));
        }
        if (year != null) {
            filmStream = filmStream.filter(film -> film.getReleaseDate().getYear() == year);
        }
        return filmStream.sorted((film1, film2) -> Integer.compare(film2.getLikesByUsers()
                .size(), film1.getLikesByUsers().size()))
                .limit(count)
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public Collection<FilmDto> getCommonFilms(Long userId, Long friendId) {
        Collection<Film> films = filmStorage.getCommonFilms(userId, friendId);
        return films.stream()
                .sorted((f1, f2) -> f2.getLikesByUsers().size() - f1.getLikesByUsers().size())
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public Collection<FilmDto> getFilmsByDirector(Long directorId, String sortBy) {
        Collection<FilmDto> result = directorsFilmsDbStorage.getDirectorOnFilm(directorId)
                .stream()
                .peek(filmStorage::addGenresAndLikes)
                .map(FilmMapper::mapToFilmDto)
                .toList();
        if ("year".equals(sortBy)) {
            return result;
        }
        return result.stream().sorted((f1, f2) -> f2.getLikesByUsers().size() - f1.getLikesByUsers().size())
                .toList();
    }
}
