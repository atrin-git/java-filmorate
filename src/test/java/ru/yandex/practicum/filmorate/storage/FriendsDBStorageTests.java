package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FriendsDbStorage;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.FriendRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.GenerateTestData;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FriendsDbStorage.class, FriendRowMapper.class, UserDbStorage.class, UserRowMapper.class})
class FriendsDBStorageTests {
    private final FriendsDbStorage friendsStorage;
    private final UserDbStorage userStorage;

    @Test
    public void checkAddFriend() {
        User user = userStorage.create(GenerateTestData.generateNewUser(List.of(1L)));
        User friend = userStorage.create(GenerateTestData.generateNewUser(List.of(2L)));

        User createdUser = userStorage.create(user);
        User createdFriend = userStorage.create(friend);

        friendsStorage.addFriend(createdUser, createdFriend);

        Optional<User> userOptional = userStorage.find(createdUser.getId());
        Optional<User> friendOptional = userStorage.find(createdFriend.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertTrue(u.getFriends().contains(createdFriend.getId()))
                );
        assertThat(friendOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertFalse(f.getFriends().contains(createdUser.getId()))
                );
    }

    @Test
    public void checkAddFriendBoth() {
        User user = userStorage.create(GenerateTestData.generateNewUser(List.of(1L)));
        User friend = userStorage.create(GenerateTestData.generateNewUser(List.of(2L)));

        User createdUser = userStorage.create(user);
        User createdFriend = userStorage.create(friend);

        friendsStorage.addFriend(createdUser, createdFriend);
        friendsStorage.addFriend(createdFriend, createdUser);

        Optional<User> userOptional = userStorage.find(createdUser.getId());
        Optional<User> friendOptional = userStorage.find(createdFriend.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertTrue(u.getFriends().contains(createdFriend.getId()))
                );
        assertThat(friendOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertTrue(f.getFriends().contains(createdUser.getId()))
                );
    }

    @Test
    public void checkRemoveFriend() {
        User user = userStorage.create(GenerateTestData.generateNewUser(List.of(1L)));
        User friend = userStorage.create(GenerateTestData.generateNewUser(List.of(2L)));

        User createdUser = userStorage.create(user);
        User createdFriend = userStorage.create(friend);

        friendsStorage.addFriend(createdUser, createdFriend);
        friendsStorage.removeFriend(createdUser, createdFriend);

        Optional<User> userOptional = userStorage.find(createdUser.getId());
        Optional<User> friendOptional = userStorage.find(createdFriend.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertFalse(u.getFriends().contains(createdFriend.getId()))
                );
        assertThat(friendOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertFalse(f.getFriends().contains(createdUser.getId()))
                );
    }

}
