package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FilmTests {
    @Test
    public void checkCreateNewFilm() {
        Film film = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(200)
                .rating(Rates.builder().id(1L).build())
                .genres(Set.of(Genre.builder().id(1L).build()))
                .likesByUsers(Set.of(1L, 2L))
                .build();

        assertNotNull(film);
        assertNotNull(film.getId());
        assertNotNull(film.getName());
        assertNotNull(film.getDescription());
        assertNotNull(film.getReleaseDate());
        assertNotNull(film.getDuration());
    }
}
