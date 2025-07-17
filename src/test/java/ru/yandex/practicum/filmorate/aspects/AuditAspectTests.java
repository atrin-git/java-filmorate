package ru.yandex.practicum.filmorate.aspects;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utils.GenerateTestData;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@EnableAspectJAutoProxy
public class AuditAspectTests {

    @MockBean
    private UserDbStorage userStorage;
    @MockBean
    private FilmDbStorage filmStorage;
    @MockBean
    private FriendsDbStorage friendsStorage;
    @MockBean
    private LikesDbStorage likesDbStorage;
    @MockBean
    private ReviewDbStorage reviewStorage;
    @MockBean
    private AuditDbStorage auditDbStorage;

    @Autowired
    private UserService userService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private ReviewService reviewService;

    private User user;
    private User friend;
    private Film film;
    private Review review;

    @BeforeEach
    public void setUp() {
        user = GenerateTestData.generateNewUser(List.of(1L));
        friend = GenerateTestData.generateNewUser(List.of(2L));
        film = GenerateTestData.generateNewFilm(List.of(1L));
        review = GenerateTestData.generateNewReview(List.of(1L), user.getId(), film.getId(), true);

        when(userStorage.find(user.getId())).thenReturn(Optional.of(user));
        when(userStorage.find(friend.getId())).thenReturn(Optional.of(friend));

        when(filmStorage.find(film.getId())).thenReturn(Optional.of(film));

        doNothing().when(likesDbStorage).addLikeOnFilm(anyLong(), anyLong());
        doNothing().when(likesDbStorage).removeLikeOnFilm(anyLong(), anyLong());

        when(reviewStorage.find(review.getId())).thenReturn(Optional.of(review));

        doNothing().when(auditDbStorage).addEvent(anyLong(), any(), any(), anyLong());
    }

    @Test
    public void checkAddNewEvent_FriendAdd() {
        userService.addFriend(user.getId(), friend.getId());

        verify(auditDbStorage, times(1))
                .addEvent(
                        eq(user.getId()),
                        eq(Events.FRIEND),
                        eq(Operations.ADD),
                        eq(friend.getId())
                );
    }

    @Test
    public void checkAddNewEvent_FriendRemove() {
        userService.addFriend(user.getId(), friend.getId());
        userService.deleteFriend(user.getId(), friend.getId());

        verify(auditDbStorage, times(1))
                .addEvent(
                        eq(user.getId()),
                        eq(Events.FRIEND),
                        eq(Operations.REMOVE),
                        eq(friend.getId())
                );
    }

    @Test
    public void checkAddNewEvent_LikeAdd() {
        filmService.addLike(film.getId(), user.getId());

        verify(auditDbStorage, times(1))
                .addEvent(
                        eq(user.getId()),
                        eq(Events.LIKE),
                        eq(Operations.ADD),
                        eq(film.getId())
                );
    }

    @Test
    public void checkAddNewEvent_LikeRemove() {
        filmService.addLike(film.getId(), user.getId());
        filmService.deleteLike(film.getId(), user.getId());

        verify(auditDbStorage, times(1))
                .addEvent(
                        eq(user.getId()),
                        eq(Events.LIKE),
                        eq(Operations.REMOVE),
                        eq(film.getId())
                );
    }

    @Test
    public void checkAddNewEvent_ReviewAdd() {
        reviewService.create(review);

        verify(auditDbStorage, times(1))
                .addEvent(
                        eq(user.getId()),
                        eq(Events.REVIEW),
                        eq(Operations.ADD),
                        eq(review.getId())
                );
    }

    @Test
    public void checkAddNewEvent_ReviewUpdate() {
        reviewService.update(review);

        verify(auditDbStorage, times(1))
                .addEvent(
                        eq(user.getId()),
                        eq(Events.REVIEW),
                        eq(Operations.UPDATE),
                        eq(review.getId())
                );
    }

    @Test
    public void checkAddNewEvent_ReviewRemove() {
        reviewService.delete(review.getId());

        verify(auditDbStorage, times(1))
                .addEvent(
                        eq(user.getId()),
                        eq(Events.REVIEW),
                        eq(Operations.REMOVE),
                        eq(review.getId())
                );
    }
}
