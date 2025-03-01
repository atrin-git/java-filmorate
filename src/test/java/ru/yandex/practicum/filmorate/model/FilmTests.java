package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FilmTests {
    @Test
    public void checkCreateNewFilm() {
        Film film = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1990,1,1))
                .duration(Duration.ofHours(2))
                .build();

        assertNotNull(film);
        assertNotNull(film.getId());
        assertNotNull(film.getName());
        assertNotNull(film.getDescription());
        assertNotNull(film.getReleaseDate());
        assertNotNull(film.getDuration());
    }
}
