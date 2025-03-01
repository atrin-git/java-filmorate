package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.utils.IdentityUtil.getNextId;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {

        log.info("Начинаем добавление пользователя");

        if (isUserNotValid(user)) {
            log.info("Валидация не пройдена. Пользователь не добавлен");
            return user;
        }

        if (users.values().stream().anyMatch(f -> f.getEmail().equalsIgnoreCase(user.getEmail()))) {
            log.info("Пользователь с email = {} уже был добавлен", user.getEmail());
            throw new DuplicateException("Пользователь с такой электронной почтой уже был добавлен");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            log.info("Имя пользователя не задано. Дублируем в имя логин.");
            user.setName(user.getLogin());
        }

        user.setId(getNextId(users.keySet()));
        users.put(user.getId(), user);

        log.info("Пользователь c email = {} добавлен", user.getEmail());

        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {

        log.info("Начинаем обновление пользователя с id = {}", user.getId());

        if (user.getId() == null || user.getId() <= 0) {
            log.debug("Передано значение id = {}. Обновление прерывается", user.getId());
            throw new ValidationException("Идентификатор пользователя должен быть определён и положительным");
        }

        if (isUserNotValid(user)) {
            log.info("Валидация не пройдена. Пользователь не обновлён");
            return user;
        }

        if (!users.containsKey(user.getId())) {
            log.info("Пользователь с id = {} не найден", user.getId());
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }

        users.put(user.getId(), user);

        log.info("Пользователь с id = {} обновлён", user.getId());

        return user;
    }

    private boolean isUserNotValid(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.debug("Передано значение email = {}. Валидация не пройдена", user.getEmail());
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.debug("Передано значение login = {}. Валидация не пройдена", user.getLogin());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Передано значение birthday = {}. Валидация не пройдена", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        return false;
    }
}
