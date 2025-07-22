package ru.yandex.practicum.filmorate.model.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmValidator {
    public static final int REQUIRED_DESCRIPTION_LENGTH = 200;
    public static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    public static void checkFilmIsValid(Film film) {
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
    }
}
