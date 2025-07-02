package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.mappers.UserMapper;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.validation.UserValidator;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Autowired
    @Qualifier("db")
    private UserStorage userStorage;
    @Autowired
    @Qualifier("db-friends")
    private FriendsStorage friendStorage;
    @Autowired
    private UserValidator userValidator;

    public Collection<UserDto> findAll() {
        return userStorage.getAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto find(Long id) {
        return userStorage.find(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));
    }

    public UserDto create(NewUserRequest newUser) {
        User user = UserMapper.mapToUser(newUser);
        userValidator.checkUserIsValid(user);
        Collection<User> users = userStorage.getAll();

        if (users.stream().map(User::getEmail).collect(Collectors.toSet()).contains(user.getEmail())) {
            throw new DuplicateException("Пользователь ранее уже был добавлен");
        }

        user = userStorage.create(user);

        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UpdateUserRequest updateUser) {
        User user = UserMapper.mapToUser(updateUser);
        userValidator.checkUserIsValid(user);

        User oldUser = userStorage.find(updateUser.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + updateUser.getId()));

        user = UserMapper.updateUserFields(oldUser, updateUser);

        return UserMapper.mapToUserDto(userStorage.update(user));
    }

    public Collection<UserDto> getFriends(Long userId) {
        final User user = userStorage.find(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));

        return userStorage.getAll().stream()
                .filter(u -> user.getFriends().contains(u.getId()))
                .map(UserMapper::mapToUserDto)
                .toList();
    }

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

}
