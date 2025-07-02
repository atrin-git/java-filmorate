package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.utils.Utils.clearStringData;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmDto> findAll() {
        log.info("Получен запрос на получение данных о всех фильмах");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public FilmDto find(@PathVariable("id") Long filmId) {
        log.info("Получен запрос на получение данных о фильме {}", filmId);
        return filmService.find(filmId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto create(@RequestBody NewFilmRequest film) {
        log.info("Получен запрос на добавление фильма");
        clearStringData(film);
        return filmService.create(film);
    }

    @PutMapping
    public FilmDto update(@RequestBody UpdateFilmRequest film) {
        log.info("Получен запрос на обновление фильма с id = {}", film.getId());
        clearStringData(film);
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        log.info("Получен запрос на добавление лайка для фильма с id = {}", filmId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        log.info("Получен запрос на удаление лайка для фильма с id = {}", filmId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> getPopularFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        log.info("Получен запрос на список популярных фильмов в количестве {} шт.", count);
        return filmService.getPopularFilms(count);
    }

}
