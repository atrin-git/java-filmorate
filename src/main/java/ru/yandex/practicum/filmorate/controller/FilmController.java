package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.utils.IdentityUtil.getNextId;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    public static final int REQUIRED_DESCRIPTION_LENGTH = 200;
    public static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        log.info("Начинаем добавление фильма");

        if (isFilmNotValid(film)) {
            log.info("Валидация не пройдена. Фильм не добавлен");
            return film;
        }

        if (films.values().stream().anyMatch(f -> f.getName().equalsIgnoreCase(film.getName()))) {
            log.info("Фильм с названием \"{}\" уже был добавлен", film.getName());
            throw new DuplicateException("Такой фильм уже был добавлен");
        }

        film.setId(getNextId(films.keySet()));
        films.put(film.getId(), film);

        log.info("Фильм \"{}\" добавлен", film.getName());

        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {

        log.info("Начинаем обновление фильма с id = {}", film.getId());

        if (film.getId() == null || film.getId() <= 0) {
            log.debug("Передано значение id = {}. Обновление прерывается", film.getId());
            throw new ValidationException("Идентификатор фильма должен быть определён и положительным");
        }

        if (isFilmNotValid(film)) {
            log.info("Валидация не пройдена. Фильм не обновлён");
            return film;
        }

        if (!films.containsKey(film.getId())) {
            log.info("Фильм с id = {} не найден", film.getId());
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }

        films.put(film.getId(), film);

        log.info("Фильм c id = {} обновлён", film.getId());

        return film;
    }

    private boolean isFilmNotValid(Film film) {

        if (film.getName() == null || film.getName().isEmpty()) {
            log.debug("Передано значение name = {}. Валидация не пройдена", film.getName());
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription() != null && film.getDescription().length() > REQUIRED_DESCRIPTION_LENGTH) {
            log.debug("Передано значение description = {}. Валидация не пройдена", film.getName());
            throw new ValidationException("Описание не может быть длиннее " + REQUIRED_DESCRIPTION_LENGTH);
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(FIRST_FILM_DATE)) {
            log.debug("Передано значение releaseDate = {}. Валидация не пройдена", film.getReleaseDate());
            throw new ValidationException("Фильмы до " + FIRST_FILM_DATE + " не существовали");
        }

        if (film.getDuration() != null && !film.getDuration().isPositive()) {
            log.debug("Передано значение duration = {}. Валидация не пройдена", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть больше 0");
        }

        return false;
    }

}
