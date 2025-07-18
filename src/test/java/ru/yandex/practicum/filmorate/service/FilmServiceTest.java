package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @Mock
    private FilmDbStorage filmStorage;

    @InjectMocks
    private FilmService filmService;

    private Film film1;
    private Film film2;
    private Film film3;

    @BeforeEach
    public void setUp() {
        film1 = Film.builder()
                .id(1L)
                .name("Фильм 1")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .likesByUsers(Set.of(1L, 2L, 3L))
                .genres(Set.of(Genre.builder().id(1L).build()))
                .build();

        film2 = Film.builder()
                .id(2L)
                .name("Фильм 2")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .likesByUsers(Set.of(1L))
                .genres(Set.of(Genre.builder().id(2L).build()))
                .build();

        film3 = Film.builder()
                .id(3L)
                .name("Фильм 3")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .likesByUsers(Set.of(1L, 2L))
                .genres(Set.of(Genre.builder().id(1L).build()))
                .build();
    }

    @Test
    public void testGetPopularFilms_AllFilms() {
        when(filmStorage.getAll()).thenReturn(List.of(film1, film2, film3));
        Collection<FilmDto> result = filmService.getPopularFilms(3L, null, null);

        assertEquals(3, result.size());
        assertEquals(film1.getId(), result.iterator().next().getId());
    }

    @Test
    public void testGetPopularFilms_ByGenre() {
        when(filmStorage.getAll()).thenReturn(List.of(film1, film3));
        Collection<FilmDto> result = filmService.getPopularFilms(2L, 1L, null);

        assertEquals(2, result.size());
        for (FilmDto film : result) {
            boolean hasGenre = film.getGenres().stream()
                    .anyMatch(genre -> genre.getId().equals(1L));
            assertTrue(hasGenre, "Фильм " + film.getId() + " не содержит жанр 1");
        }
    }

    @Test
    public void testGetPopularFilms_ByYear() {
        when(filmStorage.getAll()).thenReturn(List.of(film1, film2));
        Collection<FilmDto> result = filmService.getPopularFilms(2L, null, 2023L);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(f -> f.getReleaseDate().getYear() == 2023));
    }

    @Test
    public void testGetPopularFilms_ByGenreAndYear() {
        when(filmStorage.getAll()).thenReturn(List.of(film1, film3));
        Collection<FilmDto> result = filmService.getPopularFilms(2L, 1L, 2023L);

        assertEquals(1, result.size());
        assertEquals(film1.getId(), result.iterator().next().getId());
    }

    @Test
    public void testGetPopularFilms_Limit() {
        when(filmStorage.getAll()).thenReturn(List.of(film1, film2, film3));
        Collection<FilmDto> result = filmService.getPopularFilms(2L, null, null);

        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteFilm_ById() {
        when(filmStorage.find(1L)).thenReturn(Optional.of(film1));
        doNothing().when(filmStorage).delete(1L);
        filmService.delete(1L);

        verify(filmStorage).delete(1L);
    }

    @Test
    public void testDeleteFilm_ById_ShouldThrownNotFoundException() {
        when(filmStorage.find(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> filmService.delete(1L));
        verify(filmStorage, never()).delete(1L);
    }
}
