package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.utils.GenerateTestData.*;

@SpringBootTest
class UserControllerTests {

    private UserController userController;
    private final Map<Long, User> testUsers = new HashMap<>();

    @BeforeEach
    public void setUp() {
        userController = new UserController();
    }

    @Test
    public void checkFindAllUserNoUsers() {
        Collection<User> actualUsers = userController.findAll();

        assertNotNull(actualUsers);
        assertEquals(0, actualUsers.size());
    }

    @Test
    public void checkFindAllUserExists() {
        User user = generateNewUser(testUsers.keySet());
        userController.create(user);

        Collection<User> actualUsers = userController.findAll();
        assertNotNull(actualUsers);
        assertEquals(1, actualUsers.size());
        assertTrue(actualUsers.contains(user));
    }

    @Test
    public void checkCreateUserSuccessCreation() {
        User user = generateNewUser(testUsers.keySet());

        assertDoesNotThrow(
                () -> userController.create(user),
                "Не ожидалось срабатывание исключений");

        Collection<User> actualUsers = userController.findAll();

        assertTrue(actualUsers.contains(user));
        assertEquals(1, actualUsers.size());
    }

    @Test
    public void checkCreateUserEmailIsNull() {
        User user = generateNewUser(testUsers.keySet());
        user.setEmail(null);

        assertThrows(ValidationException.class,
                () -> userController.create(user),
                "Ожидалось исключение типа " + ValidationException.class);

        Collection<User> actualUsers = userController.findAll();

        assertTrue(actualUsers.isEmpty());
    }

    @Test
    public void checkCreateUserEmailIsEmpty() {
        User user = generateNewUser(testUsers.keySet());
        user.setEmail("");

        assertThrows(ValidationException.class,
                () -> userController.create(user),
                "Ожидалось исключение типа " + ValidationException.class);

        Collection<User> actualUsers = userController.findAll();

        assertTrue(actualUsers.isEmpty());
    }

    @Test
    public void checkCreateUserEmailHasNotAt() {
        User user = generateNewUser(testUsers.keySet());
        user.setEmail(generateString(10));

        assertThrows(ValidationException.class,
                () -> userController.create(user),
                "Ожидалось исключение типа " + ValidationException.class);

        Collection<User> actualUsers = userController.findAll();

        assertTrue(actualUsers.isEmpty());
    }

    @Test
    public void checkCreateUserLoginIsNull() {
        User user = generateNewUser(testUsers.keySet());
        user.setLogin(null);

        assertThrows(ValidationException.class,
                () -> userController.create(user),
                "Ожидалось исключение типа " + ValidationException.class);

        Collection<User> actualUsers = userController.findAll();

        assertTrue(actualUsers.isEmpty());
    }

    @Test
    public void checkCreateUserLoginIsEmpty() {
        User user = generateNewUser(testUsers.keySet());
        user.setLogin("");

        assertThrows(ValidationException.class,
                () -> userController.create(user),
                "Ожидалось исключение типа " + ValidationException.class);

        Collection<User> actualUsers = userController.findAll();

        assertTrue(actualUsers.isEmpty());
    }

    @Test
    public void checkCreateUserLoginWithSpacesOnStart() {
        User user = generateNewUser(testUsers.keySet());
        user.setLogin(" " + user.getLogin());

        assertThrows(ValidationException.class,
                () -> userController.create(user),
                "Ожидалось исключение типа " + ValidationException.class);

        Collection<User> actualUsers = userController.findAll();

        assertTrue(actualUsers.isEmpty());
    }

    @Test
    public void checkCreateUserLoginWithSpacesOnEnd() {
        User user = generateNewUser(testUsers.keySet());
        user.setLogin(user.getLogin() + " ");

        assertThrows(ValidationException.class,
                () -> userController.create(user),
                "Ожидалось исключение типа " + ValidationException.class);

        Collection<User> actualUsers = userController.findAll();

        assertTrue(actualUsers.isEmpty());
    }

    @Test
    public void checkCreateUserLoginWithSpacesInside() {
        User user = generateNewUser(testUsers.keySet());
        user.setLogin(user.getLogin().substring(0, 2) + " " + user.getLogin().substring(2));

        assertThrows(ValidationException.class,
                () -> userController.create(user),
                "Ожидалось исключение типа " + ValidationException.class);

        Collection<User> actualUsers = userController.findAll();

        assertTrue(actualUsers.isEmpty());
    }

    @Test
    public void checkCreateUserBirthdayAfterToday() {
        User user = generateNewUser(testUsers.keySet());
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ValidationException.class,
                () -> userController.create(user),
                "Ожидалось исключение типа " + ValidationException.class);

        Collection<User> actualUsers = userController.findAll();

        assertTrue(actualUsers.isEmpty());
    }

    @Test
    public void checkCreateUserAlreadyExist() {
        User user = generateNewUser(testUsers.keySet());
        userController.create(user);

        User newUser = generateNewUser(testUsers.keySet());
        newUser.setEmail(user.getEmail());

        assertThrows(DuplicateException.class,
                () -> userController.create(newUser),
                "Ожидалось исключение типа " + DuplicateException.class);

        Collection<User> actualUsers = userController.findAll();

        assertFalse(actualUsers.contains(newUser));
        assertEquals(1, actualUsers.size());
    }

    @Test
    public void checkCreateUserReplaceNameByLogin() {
        User user = generateNewUser(testUsers.keySet());
        user.setName(null);

        assertDoesNotThrow(
                () -> userController.create(user),
                "Не ожидалось срабатываний исключений");

        List<User> actualUsers = userController.findAll().stream().toList();

        assertTrue(actualUsers.contains(user));
        assertEquals(1, actualUsers.size());
        assertEquals(user.getLogin(), actualUsers.getFirst().getName());
    }

    @Test
    public void checkUpdateExistedUser() {
        User user = generateNewUser(testUsers.keySet());
        userController.create(user);

        User newUser = generateNewUser(testUsers.keySet());
        newUser.setId(user.getId());
        userController.update(newUser);

        assertTrue(userController.findAll().contains(newUser));
        assertEquals(1, userController.findAll().size());
    }

    @Test
    public void checkUpdateIdIsNull() {
        User user = generateNewUser(testUsers.keySet());
        userController.create(user);

        User newUser = user.toBuilder().build();
        newUser.setId(null);

        assertThrows(ValidationException.class,
                () -> userController.update(newUser),
                "Ожидалось исключение типа " + ValidationException.class);

        assertFalse(userController.findAll().contains(newUser));
    }

    @Test
    public void checkUpdateIdIsZero() {
        User user = generateNewUser(testUsers.keySet());
        user.setId(0L);

        assertThrows(ValidationException.class,
                () -> userController.update(user),
                "Ожидалось исключение типа " + ValidationException.class);

        assertFalse(userController.findAll().contains(user));
    }

    @Test
    public void checkUpdateIdIsNegative() {
        User user = generateNewUser(testUsers.keySet());
        user.setId(-1L);

        assertThrows(ValidationException.class,
                () -> userController.update(user),
                "Ожидалось исключение типа " + ValidationException.class);

        assertFalse(userController.findAll().contains(user));
    }
    @Test
    public void checkUpdateIdNotFound() {
        User user = generateNewUser(testUsers.keySet());
        user.setId(10L);

        assertThrows(NotFoundException.class,
                () -> userController.update(user),
                "Ожидалось исключение типа " + NotFoundException.class);

        assertFalse(userController.findAll().contains(user));
    }

}
