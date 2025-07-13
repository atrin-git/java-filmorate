package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.dal.mappers.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.GenerateTestData;
import java.util.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, GenresDbStorage.class, LikesDbStorage.class, RatesDbStorage.class,
        FilmRowMapper.class, GenresRowMapper.class, LikesRowMapper.class, RatesRowMapper.class,
        UserDbStorage.class, UserRowMapper.class, DirectorDbStorage.class, DirectorsFilmsDbStorage.class, DirectorsRowMapper.class})

class FilmDBStorageTests {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final LikesDbStorage likesStorage;

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
        filmStorage.create(GenerateTestData.generateNewFilm(List.of(1L)));
        filmStorage.create(GenerateTestData.generateNewFilm(List.of(2L)));
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

    @Test
    public void checkFindCommonFilms() {
        Film film = filmStorage.create(GenerateTestData.generateNewFilm(List.of(1L)));
        User user1 = userStorage.create(GenerateTestData.generateNewUser(List.of(1L)));
        User user2 = userStorage.create(GenerateTestData.generateNewUser(List.of(2L)));

        likesStorage.addLikeOnFilm(film.getId(), user1.getId());
        likesStorage.addLikeOnFilm(film.getId(), user2.getId());

        Collection<Film> films = filmStorage.getCommonFilms(user1.getId(), user2.getId());

        assertEquals(1, films.size(), "Общие фильмы не вернулись");
    }

    @Test
    public void checkGetRecommendationsForUser1_WhenCommonFilmExists() {
        User user1 = userStorage.create(GenerateTestData.generateNewUser(List.of(1L)));
        User user2 = userStorage.create(GenerateTestData.generateNewUser(List.of(2L)));

        Film commonFilm = filmStorage.create(GenerateTestData.generateNewFilm(List.of(1L)));
        Film uncommonFilm = filmStorage.create(GenerateTestData.generateNewFilm(List.of(2L)));

        likesStorage.addLikeOnFilm(commonFilm.getId(), user1.getId());
        likesStorage.addLikeOnFilm(commonFilm.getId(), user2.getId());
        likesStorage.addLikeOnFilm(uncommonFilm.getId(), user2.getId());

        Collection<Film> films = filmStorage.getRecommendedFilms(user1.getId());

        assertEquals(1, films.size());
        assertEquals(uncommonFilm.getId(), films.iterator().next().getId());
    }

    @Test
    public void checkGetRecommendationsForUser1_WhenCommonFilmDoesntExist() {
        User user1 = userStorage.create(GenerateTestData.generateNewUser(List.of(1L)));
        User user2 = userStorage.create(GenerateTestData.generateNewUser(List.of(2L)));

        Film uncommonFilm1 = filmStorage.create(GenerateTestData.generateNewFilm(List.of(1L)));
        Film uncommonFilm2 = filmStorage.create(GenerateTestData.generateNewFilm(List.of(2L)));

        likesStorage.addLikeOnFilm(uncommonFilm1.getId(), user1.getId());
        likesStorage.addLikeOnFilm(uncommonFilm2.getId(), user2.getId());

        Collection<Film> films = filmStorage.getRecommendedFilms(user1.getId());

        assertEquals(0, films.size());
    }
}
