package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.utils.Utils.getNextId;

@Slf4j
@Component("in_memory")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> userEmails = new HashSet<>();

    @Override
    public Collection<User> getAll() {
        return List.copyOf(users.values());
    }

    @Override
    public User create(User user) {
        if (userEmails.contains(user.getEmail().toLowerCase())) {
            log.warn("Пользователь с email = {} уже был добавлен", user.getEmail());
            throw new DuplicateException("Пользователь с такой электронной почтой уже был добавлен");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            log.info("Имя пользователя не задано. Дублируем в имя логин.");
            user.setName(user.getLogin());
        }

        userEmails.add(user.getEmail().toLowerCase());
        user.setId(getNextId(users.keySet()));
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.info("Пользователь c email = {} добавлен", user.getEmail());

        return user;
    }

    @Override
    public void delete(Long id) {
        if (find(id) == null) {
            return;
        }

        userEmails.remove(users.get(id).getEmail().toLowerCase());
        users.remove(id);
    }

    @Override
    public void deleteAll() {
        users.clear();
        userEmails.clear();
    }

    @Override
    public User update(User user) {
        if (find(user.getId()) == null) {
            return null;
        }

        // Случай, когда при обновлении меняется email и нужно удалить старый из коллекции email-ов
        final User oldUser = users.get(user.getId());
        if (!oldUser.getEmail().equalsIgnoreCase(user.getEmail())) {
            userEmails.remove(oldUser.getName().toLowerCase());
            userEmails.add(user.getEmail().toLowerCase());
        }

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }

        users.put(user.getId(), user);
        log.info("Пользователь с id = {} обновлён", user.getId());

        return user;
    }

    @Override
    public Optional<User> find(Long id) {
        if (id == null || id <= 0) {
            log.warn("Передано значение id = {}", id);
            throw new ValidationException("Идентификатор пользователя должен быть определён и положительным");
        }
        if (!users.containsKey(id)) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }

        return Optional.of(users.get(id));
    }

}
