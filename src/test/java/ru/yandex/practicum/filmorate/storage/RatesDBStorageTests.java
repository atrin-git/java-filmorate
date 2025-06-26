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
import ru.yandex.practicum.filmorate.model.Rates;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.filmorate.utils.GenerateTestData.generateNewFilm;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, GenresDbStorage.class, LikesDbStorage.class, RatesDbStorage.class,
        FilmRowMapper.class, GenresRowMapper.class, LikesRowMapper.class, RatesRowMapper.class})
class RatesDBStorageTests {
    private final RatesDbStorage ratesStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void checkGetAllRates() {
        Collection<Rates> rates = ratesStorage.findAll();
        assertEquals(5, rates.size());
    }

    @Test
    public void checkGetRate() {
        Optional<Rates> rate = ratesStorage.find(1L);

        assertThat(rate)
                .isPresent()
                .hasValueSatisfying(r ->
                        assertEquals("G", r.getName())
                );
    }

    @Test
    public void checkGetFilmRate() {
        Film createdfilm = filmStorage.create(generateNewFilm(List.of(1L)));
        Optional<Rates> rate = ratesStorage.getFilmRating(createdfilm.getId());

        assertThat(rate)
                .isPresent()
                .hasValueSatisfying(r ->
                        assertEquals("G", r.getName())
                );
    }

}
