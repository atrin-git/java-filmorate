package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.GenerateTestData;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class})
class UserDBStorageTests {
    private final UserDbStorage userStorage;

    @Test
    public void checkCreateAndFindUserById() {
        User createdUser = userStorage.create(GenerateTestData.generateNewUser(List.of(1L)));
        Optional<User> userOptional = userStorage.find(createdUser.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", createdUser.getId())
                );
    }

    @Test
    public void checkDeleteUser() {
        User createdUser = userStorage.create(GenerateTestData.generateNewUser(List.of(1L)));
        userStorage.delete(createdUser.getId());

        Optional<User> deletedUser = userStorage.find(createdUser.getId());

        assertEquals(Optional.empty(), deletedUser, "Удалённый пользователь не должен был быть найден");
    }

    @Test
    public void checkDeleteAllUsers() {
        User createdUser1 = userStorage.create(GenerateTestData.generateNewUser(List.of(1L)));
        User createdUser2 = userStorage.create(GenerateTestData.generateNewUser(List.of(2L)));
        userStorage.deleteAll();

        Collection<User> users = userStorage.getAll();

        assertEquals(0, users.size(), "Должны были быть удалены все пользователи");
    }

    @Test
    public void checkFindAllUsers() {
        userStorage.create(GenerateTestData.generateNewUser(List.of(1L)));
        userStorage.create(GenerateTestData.generateNewUser(List.of(2L)));
        userStorage.create(GenerateTestData.generateNewUser(List.of(3L)));

        Collection<User> users = userStorage.getAll();

        assertEquals(3, users.size(), "Вернулись не все пользователи");
    }

    @Test
    public void checkUpdateUser() {
        User user = GenerateTestData.generateNewUser(List.of(1L));
        User userCreated = userStorage.create(user);
        User updateUser = GenerateTestData.generateNewUser(List.of(2L));
        updateUser.setId(userCreated.getId());
        userStorage.update(updateUser);

        Optional<User> actualUser = userStorage.find(userCreated.getId());
        assertThat(actualUser)
                .isPresent()
                .hasValueSatisfying(value -> assertEquals(updateUser, value));
    }
}
