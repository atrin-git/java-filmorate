package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User create(User user);

    void delete(Long id);

    void deleteAll();

    User update(User user);

    User find(Long id);

    Collection<User> getAll();
}
