package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.aspects.Auditable;
import ru.yandex.practicum.filmorate.dal.*;
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

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

import static ru.yandex.practicum.filmorate.model.validation.FilmValidator.checkFilmIsValid;

@Slf4j
@Service
public class FilmService {
    @Autowired
    private FilmDbStorage filmStorage;
    @Autowired
    private UserDbStorage userStorage;
    @Autowired
    private GenresDbStorage genresStorage;
    @Autowired
    private LikesDbStorage likeStorage;
    @Autowired
    private RatesDbStorage ratesStorage;
    @Autowired
    private DirectorsFilmsDbStorage directorsFilmsDbStorage;

    public Collection<FilmDto> findAll() {
        return filmStorage.getAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public FilmDto find(Long filmId) {
        if (filmId == null || filmId < 1) {
            throw new ValidationException("Указан некорректный id пользователя");
        }
        return filmStorage.find(filmId)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> {
                    log.warn("Не найден фильм с ID = {}", filmId);
                    return new NotFoundException("Фильм не найден с ID: " + filmId);
                });

    }

    public FilmDto create(NewFilmRequest newFilm) {
        Film film = FilmMapper.mapToFilm(newFilm);
        checkFilmIsValid(film);

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
        checkFilmIsValid(film);

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
                    return new NotFoundException("Фильм не найден с ID: " + filmId);
                });

        filmStorage.delete(filmId);
    }

    @Auditable(eventName = Events.LIKE, operationName = Operations.ADD, userId = "#userId", entityId = "#filmId")
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
    }

    @Auditable(eventName = Events.LIKE, operationName = Operations.REMOVE, userId = "#userId", entityId = "#filmId")
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
    }

    public Collection<FilmDto> getPopularFilms(Long count, Long genreId, Long year) {
        return filmStorage.getAll().stream()
                .filter(film -> (genreId == null || film.getGenres().stream()
                        .anyMatch(genre -> Objects.equals(genre.getId(), genreId)))
                        && (year == null || film.getReleaseDate().getYear() == year))
                .sorted((film1, film2) -> Integer.compare(film2.getLikesByUsers().size(), film1.getLikesByUsers().size()))
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

        if (result.isEmpty()) {
            log.warn("Не найдено фильмов с режиссёром {}", directorId);
            throw new NotFoundException("Не найдено фильмов с режиссёром ID = " + directorId);
        }

        if (sortBy == null || sortBy.isEmpty()) {
            return result;
        }

        switch (sortBy) {
            case "year" -> {
                return result.stream().sorted(Comparator.comparing(FilmDto::getReleaseDate))
                        .toList();
            }
            case "likes" -> {
                return result.stream().sorted((f1, f2) -> f2.getLikesByUsers().size() - f1.getLikesByUsers().size())
                        .toList();
            }
            default -> {
                throw new ValidationException("Возможные значения сортировки sortBy: year, likes");
            }
        }
    }

    public Collection<FilmDto> searchFilmsByDirectorOrTitle(String substring, String by) {

        Collection<Film> films = filmStorage.searchFilmsByDirectorOrTitle(substring.toLowerCase(), by);

        return films.stream()
                .sorted((f1, f2) -> f2.getLikesByUsers().size() - f1.getLikesByUsers().size())
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
