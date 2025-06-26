package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.utils.Utils.getNextId;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final Set<String> filmNames = new HashSet<>();

    @Override
    public Film create(Film film) {
        if (filmNames.contains(film.getName().toLowerCase())) {
            log.warn("Фильм с названием \"{}\" уже был добавлен", film.getName());
            throw new DuplicateException("Такой фильм уже был добавлен");
        }

        filmNames.add(film.getName().toLowerCase());
        film.setId(getNextId(films.keySet()));
        film.setLikesByUsers(new HashSet<>());
        films.put(film.getId(), film);
        log.info("Фильм \"{}\" добавлен", film.getName());

        return film;
    }

    @Override
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("Идентификатор фильма должен быть определён и положительным");
        }

        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }

        filmNames.remove(films.get(id).getName().toLowerCase());
        films.remove(id);
    }

    @Override
    public void deleteAll() {
        films.clear();
        filmNames.clear();
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null || film.getId() <= 0) {
            log.warn("Передано значение id = {}. Обновление прерывается", film.getId());
            throw new ValidationException("Идентификатор фильма должен быть определён и положительным");
        }

        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }

        // Случай, когда при обновлении меняется название фильма и нужно удалить старый из коллекции названий
        final Film oldFilm = films.get(film.getId());
        if (!oldFilm.getName().equalsIgnoreCase(film.getName())) {
            filmNames.remove(oldFilm.getName().toLowerCase());
            filmNames.add(film.getName().toLowerCase());
        }
        if (film.getLikesByUsers() == null) {
            film.setLikesByUsers(new HashSet<>());
        }

        films.put(film.getId(), film);

        log.info("Фильм c id = {} обновлён", film.getId());

        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return List.copyOf(films.values());
    }

    @Override
    public Optional<Film> find(Long id) {
        if (id == null || id <= 0) {
            log.warn("Передано значение id = {}", id);
            throw new ValidationException("Идентификатор фильма должен быть определён и положительным");
        }
        if (!films.containsKey(id)) {
            log.warn("Фильм с id = {} не найден", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }

        return Optional.of(films.get(id));
    }
}
