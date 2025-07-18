package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.aspects.Auditable;
import ru.yandex.practicum.filmorate.dal.AuditDbStorage;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.FriendsDbStorage;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.dto.mappers.AuditMapper;
import ru.yandex.practicum.filmorate.dto.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.dto.mappers.UserMapper;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Events;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.model.validation.UserValidator.checkUserIsValid;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserDbStorage userStorage;
    @Autowired
    private FilmDbStorage filmStorage;
    @Autowired
    private FriendsDbStorage friendStorage;
    @Autowired
    private AuditDbStorage auditStorage;

    public Collection<UserDto> findAll() {
        return userStorage.getAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto find(Long userId) {
        if (userId == null || userId < 1) {
            throw new ValidationException("Указан некорректный id пользователя");
        }
        return userStorage.find(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
    }

    public UserDto create(NewUserRequest newUser) {
        User user = UserMapper.mapToUser(newUser);
        checkUserIsValid(user);
        Collection<User> users = userStorage.getAll();

        if (users.stream().map(User::getEmail).collect(Collectors.toSet()).contains(user.getEmail())) {
            throw new DuplicateException("Пользователь ранее уже был добавлен");
        }

        user = userStorage.create(user);

        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UpdateUserRequest updateUser) {
        User user = UserMapper.mapToUser(updateUser);
        checkUserIsValid(user);

        User oldUser = userStorage.find(updateUser.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + updateUser.getId()));

        user = UserMapper.updateUserFields(oldUser, updateUser);

        return UserMapper.mapToUserDto(userStorage.update(user));
    }

    public void delete(Long userId) {
        if (userId == null || userId < 1) {
            throw new ValidationException("Указан некорректный id пользователя");
        }
        userStorage.find(userId)
                .orElseThrow(() -> {
                    log.warn("Не найден пользователь с ID = {}", userId);
                    return new NotFoundException("Пользователь не найден с ID: " + userId);
                });

        userStorage.delete(userId);
    }

    public Collection<UserDto> getFriends(Long userId) {
        final User user = userStorage.find(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));

        return userStorage.getAll().stream()
                .filter(u -> user.getFriends().contains(u.getId()))
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Auditable(eventName = Events.FRIEND, operationName = Operations.ADD, userId = "#userId", entityId = "#friendId")
    public void addFriend(Long userId, Long friendId) {
        final User user = userStorage.find(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));

        final User friend = userStorage.find(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + friendId));

        if (user.equals(friend)) {
            log.warn("Попытка добавить пользователя самого себе в друзья: userId={}, friendId={}", userId, friendId);
            throw new DuplicateException("Нельзя добавить пользователя самого себе в друзья");
        }

        if (user.getFriends().contains(friendId)) {
            log.warn("Попытка добавить друга снова в друзья: userId={}, friendId={}", userId, friendId);
            throw new DuplicateException("Запрос на добавление в друзья ранее уже отправлялся");
        }

        friendStorage.addFriend(user, friend);
    }

    @Auditable(eventName = Events.FRIEND, operationName = Operations.REMOVE, userId = "#userId", entityId = "#friendId")
    public void deleteFriend(Long userId, Long friendId) {
        final User user = userStorage.find(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));


        final User friend = userStorage.find(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + friendId));

        if (user.equals(friend)) {
            log.warn("При удалении из друзей переданы эквивалентные значения: userId={}, friendId={}", userId, friendId);
            throw new DuplicateException("Нельзя удалить пользователя из друзей самого себя");
        }

        if (!user.getFriends().contains(friendId)) {
            return;
        }

        friendStorage.removeFriend(user, friend);
    }

    public Collection<UserDto> findCommonFriends(Long userId, Long friendId) {
        final User user = userStorage.find(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));

        final User friend = userStorage.find(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + friendId));


        Set<Long> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(friend.getFriends());

        return userStorage.getAll().stream()
                .filter(u -> intersection.contains(u.getId()))
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public Collection<FilmDto> getRecommendedFilms(Long userId) {
        Collection<Film> films = filmStorage.getRecommendedFilms(userId);
        return films.stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public Collection<AuditDto> getFeed(Long userId) {
        if (userId < 1) {
            log.warn("Передан ID = {} меньше 1", userId);
            throw new ValidationException("Идентификатор пользователя не может быть менее 1.");
        }

        find(userId);

        return auditStorage.getEventsForUser(userId).stream()
                .map(AuditMapper::mapToAuditDto)
                .toList();
    }

}