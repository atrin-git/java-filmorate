package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.utils.GenerateTestData.generateNewFilm;
import static ru.yandex.practicum.filmorate.utils.GenerateTestData.generateString;

@SpringBootTest
class FilmControllerTests {

    private FilmController filmController;
    private final Map<Long, Film> testFilms = new HashMap<>();

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }

    @Test
    public void checkFindAllFilmsNoFilms() throws Exception {
        Collection<Film> actualFilms = filmController.findAll();

        assertNotNull(actualFilms);
        assertEquals(0, actualFilms.size());
    }

    @Test
    public void checkFindAllFilmsFilmsAdded() {
        Film film = generateNewFilm(testFilms.keySet());
        filmController.create(film);

        Collection<Film> actualFilms = filmController.findAll();

        assertTrue(actualFilms.contains(film));
        assertEquals(1, actualFilms.size());
    }

    @Test
    public void checkCreateFilmNameIsNull() {
        Film film = generateNewFilm(testFilms.keySet());
        film.setName(null);

        assertThrows(ValidationException.class,
                () -> filmController.create(film),
                "Ожидалось исключение типа " + ValidationException.class);

        assertFalse(filmController.findAll().contains(film));
    }

    @Test
    public void checkCreateFilmDescriptionLengthEquals200() {
        Film film = generateNewFilm(testFilms.keySet());
        film.setDescription(generateString(200));

        filmController.create(film);

        assertTrue(filmController.findAll().contains(film));
    }

    @Test
    public void checkCreateFilmDescriptionLengthMore200() {
        Film film = generateNewFilm(testFilms.keySet());
        film.setDescription(generateString(201));

        assertThrows(ValidationException.class,
                () -> filmController.create(film),
                "Ожидалось исключение типа " + ValidationException.class);
        assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void checkCreateFilmReleaseDateBeforeFirstFilmDate() {
        Film film = generateNewFilm(testFilms.keySet());
        film.setReleaseDate(FilmController.FIRST_FILM_DATE.minusDays(1));

        assertThrows(ValidationException.class,
                () -> filmController.create(film),
                "Ожидалось исключение типа " + ValidationException.class);
        assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void checkCreateFilmDurationIsZero() {
        Film film = generateNewFilm(testFilms.keySet());
        film.setDuration(0);

        assertThrows(ValidationException.class,
                () -> filmController.create(film),
                "Ожидалось исключение типа " + ValidationException.class);
        assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void checkCreateFilmDurationIsNegative() {
        Film film = generateNewFilm(testFilms.keySet());
        film.setDuration(-1);

        assertThrows(ValidationException.class,
                () -> filmController.create(film),
                "Ожидалось исключение типа " + ValidationException.class);
        assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void checkCreateFilmWithExistedName() {
        Film film = generateNewFilm(testFilms.keySet());
        filmController.create(film);
        Film newFilm = generateNewFilm(testFilms.keySet());
        newFilm.setName(film.getName());

        assertThrows(DuplicateException.class,
                () -> filmController.create(film),
                "Ожидалось исключение типа " + DuplicateException.class);
        assertFalse(filmController.findAll().contains(newFilm));
    }

    @Test
    public void checkUpdateExistedFilm() {
        Film film = generateNewFilm(testFilms.keySet());
        filmController.create(film);

        Film newFilm = generateNewFilm(testFilms.keySet());
        newFilm.setId(film.getId());
        filmController.update(newFilm);

        assertTrue(filmController.findAll().contains(newFilm));
        assertEquals(1, filmController.findAll().size());
    }

    @Test
    public void checkUpdateIdIsNull() {
        Film film = generateNewFilm(testFilms.keySet());
        filmController.create(film);

        Film newFilm = film.toBuilder().build();
        newFilm.setId(null);

        assertThrows(ValidationException.class,
                () -> filmController.update(newFilm),
                "Ожидалось исключение типа " + ValidationException.class);

        assertFalse(filmController.findAll().contains(newFilm));
    }

    @Test
    public void checkUpdateIdIsZero() {
        Film film = generateNewFilm(testFilms.keySet());
        film.setId(0L);

        assertThrows(ValidationException.class,
                () -> filmController.update(film),
                "Ожидалось исключение типа " + ValidationException.class);

        assertFalse(filmController.findAll().contains(film));
    }

    @Test
    public void checkUpdateIdIsNegative() {
        Film film = generateNewFilm(testFilms.keySet());
        film.setId(-1L);

        assertThrows(ValidationException.class,
                () -> filmController.update(film),
                "Ожидалось исключение типа " + ValidationException.class);

        assertFalse(filmController.findAll().contains(film));
    }

    @Test
    public void checkUpdateIdNotFound() {
        Film film = generateNewFilm(testFilms.keySet());
        film.setId(10L);

        assertThrows(NotFoundException.class,
                () -> filmController.update(film),
                "Ожидалось исключение типа " + NotFoundException.class);

        assertFalse(filmController.findAll().contains(film));
    }

}
