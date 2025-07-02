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
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yandex.practicum.filmorate.utils.GenerateTestData.generateNewFilm;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, GenresDbStorage.class, LikesDbStorage.class, RatesDbStorage.class,
        FilmRowMapper.class, GenresRowMapper.class, LikesRowMapper.class, RatesRowMapper.class})
class GenresDBStorageTests {
    private final GenresDbStorage genresStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void checkFindAllGenres() {
        Collection<Genre> genres = genresStorage.findAll();

        assertEquals(6, genres.size());
    }

    @Test
    public void checkFindGenre() {
        Optional<Genre> genre = genresStorage.find(1L);

        assertThat(genre)
                .isPresent()
                .hasValueSatisfying(value ->
                        assertEquals("Комедия", value.getName())
                );
    }

    @Test
    public void checkSetAndGetGenresForFilm() {
        Film filmCreated = filmStorage.create(generateNewFilm(List.of(1L)));
        genresStorage.setGenresForFilm(filmCreated.getId(), Set.of(Genre.builder().id(2L).build()));

        Collection<Genre> genres = genresStorage.getGenresForFilm(filmCreated.getId());

        assertEquals(2, genres.size());
        assertTrue(genres.stream().map(Genre::getId).collect(Collectors.toSet()).contains(2L));
    }

}
