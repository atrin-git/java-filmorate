package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.utils.GenerateTestData.generateNewFilm;

@WebMvcTest(FilmController.class)
public class FilmControllerExceptionHandlersTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Map<Long, Film> testFilms = new HashMap<>();
    private final String URL = "/films";

    @Test
    public void checkFindAllCodeIsSuccess() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk());
    }

    @Test
    public void checkCreateCodeIsBadRequest() throws Exception {
        mockMvc.perform(post(URL))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkCreateCodeIsConflict() throws Exception {
        Film film = generateNewFilm(Collections.singleton(0L));
        mockMvc.perform(post(URL)
                .content(objectMapper.writeValueAsString(film))
                .contentType(MediaType.APPLICATION_JSON));

        Film newFilm = generateNewFilm(Collections.singleton(1L));
        newFilm.setName(film.getName());

        mockMvc.perform(post(URL)
                        .content(objectMapper.writeValueAsString(newFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());
    }

    @Test
    public void checkUpdateCodeIsNotFound() throws Exception {
        Film film = generateNewFilm(testFilms.keySet());
        film.setId(10L);

        mockMvc.perform(put(URL)
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
