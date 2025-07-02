package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface FriendsStorage {
    void addFriend(User user, User friend);

    void removeFriend(User user, User friend);

}
