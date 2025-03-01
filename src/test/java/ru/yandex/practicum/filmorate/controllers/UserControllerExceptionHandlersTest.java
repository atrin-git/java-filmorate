package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.utils.GenerateTestData.generateNewFilm;
import static ru.yandex.practicum.filmorate.utils.GenerateTestData.generateNewUser;

@WebMvcTest(UserController.class)
public class UserControllerExceptionHandlersTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Map<Long, Film> testUsers = new HashMap<>();
    private final String url = "/users";

    @Test
    public void checkFindAllCodeIsSuccess() throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isOk());
    }

    @Test
    public void checkCreateCodeIsBadRequest() throws Exception {
        mockMvc.perform(post(url))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkCreateCodeIsConflict() throws Exception {
        User user = generateNewUser(Collections.singleton(0L));
        mockMvc.perform(post(url)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON));

        User newUser = generateNewUser(Collections.singleton(1L));
        newUser.setEmail(user.getEmail());

        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(newUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());
    }

    @Test
    public void checkUpdateCodeIsNotFound() throws Exception {
        User user = generateNewUser(testUsers.keySet());
        user.setId(10L);

        mockMvc.perform(put(url)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
