package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component("db")
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(name, login, email, birthday) " +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET name = ?, login = ?, email = ?, password = ?, birthday = ? WHERE id = ?";
    private static final String FIND_FRIENDS_QUERY = "SELECT * FROM users WHERE id IN (" +
            "SELECT friend_id FROM friends WHERE user_id = ? )";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String DELETE_ALL_QUERY = "DELETE FROM users";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User create(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday());

        user.setId(id);
        return user;
    }

    @Override
    public void delete(Long id) {
        delete(
                DELETE_QUERY,
                id
        );
    }

    @Override
    public void deleteAll() {
        deleteAll(DELETE_ALL_QUERY);
    }

    @Override
    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getPassword(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public Optional<User> find(Long id) {
        Optional<User> user = findOne(
                FIND_QUERY,
                id
        );

        user.ifPresent(value -> value.setFriends(
                findMany(FIND_FRIENDS_QUERY, value.getId())
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toSet())
        ));

        return user;
    }

    @Override
    public Collection<User> getAll() {
        Collection<User> users = findMany(FIND_ALL_QUERY);

        if (users.isEmpty()) {
            return users;
        }

        users.forEach(user -> {
            user.setFriends(
                    findMany(FIND_FRIENDS_QUERY, user.getId())
                            .stream()
                            .map(User::getId)
                            .collect(Collectors.toSet())
            );
        });

        return users;
    }

}
