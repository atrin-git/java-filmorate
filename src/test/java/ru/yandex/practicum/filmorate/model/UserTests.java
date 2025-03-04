package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserTests {
    @Test
    public void checkCreateNewUser() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .login("login")
                .email("e@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertNotNull(user.getLogin());
        assertNotNull(user.getEmail());
        assertNotNull(user.getBirthday());
    }
}
