package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.validation.UserValidator;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final UserValidator userValidator;

    public Collection<User> findAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        userValidator.checkUserIsValid(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        userValidator.checkUserIsValid(user);
        return userStorage.update(user);
    }

    public void deleteAll() {
        userStorage.getAll().stream().map(User::getId).forEach(userStorage::delete);
    }

    public Collection<User> getFriends(Long userId) {
        final User user = userStorage.find(userId);
        if (user == null) {
            return null;
        }

        return userStorage.getAll().stream().filter(u -> user.getFriends().contains(u.getId())).toList();
    }

    public void addFriend(Long userId, Long friendId) {
        final User user = userStorage.find(userId);
        if (user == null) {
            return;
        }
        final User friend = userStorage.find(friendId);
        if (friend == null) {
            return;
        }

        if (user.equals(friend)) {
            log.warn("Попытка добавить пользователя самого себе в друзья: userId={}, friendId={}", userId, friendId);
            throw new DuplicateException("Нельзя добавить пользователя самого себе в друзья");
        }

        Set<Long> userFriends = user.getFriends();
        userFriends.add(friendId);
        user.setFriends(userFriends);
        userStorage.update(user);

        Set<Long> friendFriends = friend.getFriends();
        friendFriends.add(userId);
        friend.setFriends(friendFriends);
        userStorage.update(friend);
    }

    public void deleteFriend(Long userId, Long friendId) {
        final User user = userStorage.find(userId);
        if (user == null) {
            return;
        }

        final User friend = userStorage.find(friendId);
        if (friend == null) {
            return;
        }
        if (user.equals(friend)) {
            log.warn("При удалении из друзей переданы эквивалентные значения: userId={}, friendId={}", userId, friendId);
            throw new DuplicateException("Нельзя удалить пользователя из друзей самого себя");
        }

        Set<Long> userFriends = user.getFriends();
        userFriends.remove(friendId);
        user.setFriends(userFriends);
        userStorage.update(user);

        Set<Long> friendFriends = friend.getFriends();
        friendFriends.remove(userId);
        friend.setFriends(friendFriends);
        userStorage.update(friend);
    }

    public Collection<User> findCommonFriends(Long userId, Long friendId) {
        final User user = userStorage.find(userId);
        if (user == null) {
            return null;
        }
        final User friend = userStorage.find(friendId);
        if (friend == null) {
            return null;
        }

        Set<Long> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(friend.getFriends());

        return userStorage.getAll().stream().filter(u -> intersection.contains(u.getId())).toList();
    }
}
