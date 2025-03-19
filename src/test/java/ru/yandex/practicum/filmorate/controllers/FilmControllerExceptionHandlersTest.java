package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.validation.FilmValidator;
import ru.yandex.practicum.filmorate.model.validation.UserValidator;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryUserStorage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.utils.GenerateTestData.generateNewFilm;
import static ru.yandex.practicum.filmorate.utils.GenerateTestData.generateNewUser;

@WebMvcTest(FilmController.class)
@Import({FilmService.class, InMemoryFilmStorage.class, FilmValidator.class,
        UserService.class, InMemoryUserStorage.class, UserValidator.class})
public class FilmControllerExceptionHandlersTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private FilmService filmService;

    @BeforeEach
    public void setUp() {
        userService.deleteAll();
        filmService.deleteAll();
    }

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Map<Long, Film> testFilms = new HashMap<>();
    private final String urlFilms = "/films";

    @Test
    public void checkFindAllCodeIsSuccess() throws Exception {
        mockMvc.perform(get(urlFilms))
                .andExpect(status().isOk());
    }

    @Test
    public void checkCreateCodeIsBadRequest() throws Exception {
        mockMvc.perform(post(urlFilms))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkCreateCodeIsConflict() throws Exception {
        Film film = generateNewFilm(Collections.singleton(0L));
        mockMvc.perform(post(urlFilms)
                .content(objectMapper.writeValueAsString(film))
                .contentType(MediaType.APPLICATION_JSON));

        Film newFilm = generateNewFilm(Collections.singleton(1L));
        newFilm.setName(film.getName());

        mockMvc.perform(post(urlFilms)
                        .content(objectMapper.writeValueAsString(newFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());
    }

    @Test
    public void checkUpdateCodeIsNotFound() throws Exception {
        Film film = generateNewFilm(testFilms.keySet());
        film.setId(10L);

        mockMvc.perform(put(urlFilms)
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkAddLikeSuccess() throws Exception {
        Film film = generateNewFilm(Collections.singleton(0L));
        User user = generateNewUser(Collections.singleton(0L));

        filmService.create(film);
        userService.create(user);

        mockMvc.perform(put(urlFilms + "/" + film.getId() + "/like/" + user.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkAddLikeUserNotFound() throws Exception {
        Film film = generateNewFilm(Collections.singleton(0L));
        User user = generateNewUser(Collections.singleton(0L));

        filmService.create(film);

        mockMvc.perform(put(urlFilms + "/" + film.getId() + "/like/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkAddLikeFilmNotFound() throws Exception {
        Film film = generateNewFilm(Collections.singleton(0L));
        User user = generateNewUser(Collections.singleton(0L));

        userService.create(user);

        mockMvc.perform(put(urlFilms + "/" + film.getId() + "/like/" + user.getId()))
                .andExpect(status().isNotFound());
    }


    @Test
    public void checkDeleteLikeSuccess() throws Exception {
        Film film = generateNewFilm(Collections.singleton(0L));
        User user = generateNewUser(Collections.singleton(0L));

        filmService.create(film);
        userService.create(user);

        mockMvc.perform(delete(urlFilms + "/" + film.getId() + "/like/" + user.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkDeleteLikeUserNotFound() throws Exception {
        Film film = generateNewFilm(Collections.singleton(0L));
        User user = generateNewUser(Collections.singleton(0L));

        filmService.create(film);

        mockMvc.perform(delete(urlFilms + "/" + film.getId() + "/like/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkDeleteLikeFilmNotFound() throws Exception {
        Film film = generateNewFilm(Collections.singleton(0L));
        User user = generateNewUser(Collections.singleton(0L));

        userService.create(user);

        mockMvc.perform(delete(urlFilms + "/" + film.getId() + "/like/" + user.getId()))
                .andExpect(status().isNotFound());
    }
}
