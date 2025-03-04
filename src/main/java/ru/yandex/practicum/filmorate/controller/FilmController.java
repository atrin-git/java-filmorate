package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.utils.ControllersUtils.clearStringData;
import static ru.yandex.practicum.filmorate.utils.ControllersUtils.getNextId;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    public static final int REQUIRED_DESCRIPTION_LENGTH = 200;
    public static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    private final Map<Long, Film> films = new HashMap<>();
    private final Set<String> filmNames = new HashSet<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        log.info("Начинаем добавление фильма");

        clearStringData(film);
        checkFilmIsValid(film);

        if (filmNames.contains(film.getName().toLowerCase())) {
            log.warn("Фильм с названием \"{}\" уже был добавлен", film.getName());
            throw new DuplicateException("Такой фильм уже был добавлен");
        }

        filmNames.add(film.getName().toLowerCase());
        film.setId(getNextId(films.keySet()));
        films.put(film.getId(), film);

        log.info("Фильм \"{}\" добавлен", film.getName());

        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {

        log.info("Начинаем обновление фильма с id = {}", film.getId());

        if (film.getId() == null || film.getId() <= 0) {
            log.warn("Передано значение id = {}. Обновление прерывается", film.getId());
            throw new ValidationException("Идентификатор фильма должен быть определён и положительным");
        }

        clearStringData(film);
        checkFilmIsValid(film);

        if (!films.containsKey(film.getId())) {
            log.warn("Фильм с id = {} не найден", film.getId());
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }

        // Случай, когда при обновлении меняется название фильма и нужно удалить старый из коллекции названий
        final Film oldFilm = films.get(film.getId());
        if (!oldFilm.getName().equalsIgnoreCase(film.getName())) {
            filmNames.remove(oldFilm.getName().toLowerCase());
            filmNames.add(film.getName().toLowerCase());
        }

        films.put(film.getId(), film);

        log.info("Фильм c id = {} обновлён", film.getId());

        return film;
    }

    private static void checkFilmIsValid(Film film) {
        try {
            if (film.getName() == null || film.getName().isEmpty()) {
                log.debug("Передано значение name = {}.", film.getName());
                throw new ValidationException("Название фильма не может быть пустым");
            }

            if (film.getDescription() != null && film.getDescription().length() > REQUIRED_DESCRIPTION_LENGTH) {
                log.debug("Передано значение description = {}.", film.getName());
                throw new ValidationException("Описание не может быть длиннее " + REQUIRED_DESCRIPTION_LENGTH);
            }

            if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(FIRST_FILM_DATE)) {
                log.debug("Передано значение releaseDate = {}.", film.getReleaseDate());
                throw new ValidationException("Фильмы до " + FIRST_FILM_DATE + " не существовали");
            }

            if (film.getDuration() != null && film.getDuration() <= 0) {
                log.debug("Передано значение duration = {}.", film.getDuration());
                throw new ValidationException("Продолжительность фильма должна быть больше 0");
            }
        } catch (ValidationException e) {
            log.warn("Валидация не пройдена. {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }

}
