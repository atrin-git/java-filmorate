package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

@Slf4j
@Component("db-friends")
public class FriendsDbStorage extends BaseDbStorage<Friends> implements FriendsStorage {
    private static final String ADD_FRIEND = "INSERT INTO friends(user_id, friend_id) " +
            "VALUES (?, ?)";
    private static final String REMOVE_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    public FriendsDbStorage(JdbcTemplate jdbc, RowMapper<Friends> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addFriend(User user, User friend) {
        update(
                ADD_FRIEND,
                user.getId(),
                friend.getId()
        );
    }

    @Override
    public void removeFriend(User user, User friend) {
        update(
                REMOVE_FRIEND,
                user.getId(),
                friend.getId()
        );
    }

}


