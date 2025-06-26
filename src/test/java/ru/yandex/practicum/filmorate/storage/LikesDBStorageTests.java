package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.dal.mappers.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yandex.practicum.filmorate.utils.GenerateTestData.generateNewFilm;
import static ru.yandex.practicum.filmorate.utils.GenerateTestData.generateNewUser;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, GenresDbStorage.class, LikesDbStorage.class, RatesDbStorage.class, UserDbStorage.class,
        FilmRowMapper.class, GenresRowMapper.class, LikesRowMapper.class, RatesRowMapper.class, UserRowMapper.class})
class LikesDBStorageTests {
    private final LikesDbStorage likesStorage;
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    @Test
    public void checkAddAndGetLike() {
        User userCreated = userStorage.create(generateNewUser(List.of(1L)));
        Film filmCreated = filmStorage.create(generateNewFilm(List.of(1L)));

        likesStorage.addLikeOnFilm(filmCreated.getId(), userCreated.getId());

        Collection<Likes> likes = likesStorage.getLikesOnFilm(filmCreated.getId());

        assertTrue(likes.stream().map(Likes::getUserId).collect(Collectors.toSet()).contains(userCreated.getId()));
    }

    @Test
    public void checkRemoveLike() {
        User userCreated = userStorage.create(generateNewUser(List.of(1L)));
        Film filmCreated = filmStorage.create(generateNewFilm(List.of(1L)));

        likesStorage.addLikeOnFilm(filmCreated.getId(), userCreated.getId());
        likesStorage.removeLikeOnFilm(filmCreated.getId(), userCreated.getId());

        Collection<Likes> likes = likesStorage.getLikesOnFilm(filmCreated.getId());

        assertFalse(likes.stream().map(Likes::getUserId).collect(Collectors.toSet()).contains(userCreated.getId()));
    }

}
