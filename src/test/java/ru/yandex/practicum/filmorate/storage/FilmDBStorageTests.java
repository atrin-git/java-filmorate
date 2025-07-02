package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.GenresDbStorage;
import ru.yandex.practicum.filmorate.dal.LikesDbStorage;
import ru.yandex.practicum.filmorate.dal.RatesDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenresRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.LikesRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.RatesRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.GenerateTestData;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, GenresDbStorage.class, LikesDbStorage.class, RatesDbStorage.class,
        FilmRowMapper.class, GenresRowMapper.class, LikesRowMapper.class, RatesRowMapper.class})
class FilmDBStorageTests {
    private final FilmDbStorage filmStorage;

    @Test
    public void checkCreateAndFindFilmById() {
        Film createdFilm = filmStorage.create(GenerateTestData.generateNewFilm(List.of(1L)));
        Optional<Film> filmOptional = filmStorage.find(createdFilm.getId());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", createdFilm.getId())
                );
    }

    @Test
    public void checkDeleteFilm() {
        Film createdFilm = filmStorage.create(GenerateTestData.generateNewFilm(List.of(1L)));
        filmStorage.delete(createdFilm.getId());

        Optional<Film> deletedFilm = filmStorage.find(createdFilm.getId());

        assertEquals(Optional.empty(), deletedFilm, "Удалённый фильм не должен был быть найден");
    }

    @Test
    public void checkDeleteAllFilms() {
        Film createdFilm1 = filmStorage.create(GenerateTestData.generateNewFilm(List.of(1L)));
        Film createdFilm2 = filmStorage.create(GenerateTestData.generateNewFilm(List.of(2L)));
        filmStorage.deleteAll();

        Collection<Film> films = filmStorage.getAll();

        assertEquals(0, films.size(), "Должны были быть удалены все фильмы");
    }

    @Test
    public void checkFindAllFilms() {
        filmStorage.create(GenerateTestData.generateNewFilm(List.of(1L)));
        filmStorage.create(GenerateTestData.generateNewFilm(List.of(2L)));
        filmStorage.create(GenerateTestData.generateNewFilm(List.of(3L)));

        Collection<Film> films = filmStorage.getAll();

        assertEquals(3, films.size(), "Вернулись не все фильмы");
    }

    @Test
    public void checkUpdateFilm() {
        Film film = filmStorage.create(GenerateTestData.generateNewFilm(List.of(1L)));
        Film filmCreated = filmStorage.create(film);
        Film updateFilm = GenerateTestData.generateNewFilm(List.of(2L));
        updateFilm.setId(filmCreated.getId());
        filmStorage.update(updateFilm);

        Optional<Film> actualFilm = filmStorage.find(filmCreated.getId());
        assertThat(actualFilm)
                .isPresent()
                .hasValueSatisfying(value -> assertEquals(updateFilm, value));
    }

}
