package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dal.AuditDbStorage;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.ReviewDbStorage;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.utils.GenerateTestData;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class AuditDBStorageTests {
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final ReviewDbStorage reviewDbStorage;
    private final AuditDbStorage auditDbStorage;

    @Test
    public void checkAddNewEvent_FriendAdd() {
        User user = userDbStorage.create(GenerateTestData.generateNewUser(List.of(1L)));
        User friend = userDbStorage.create(GenerateTestData.generateNewUser(List.of(2L)));

        auditDbStorage.addEvent(user.getId(), Events.FRIEND, Operations.ADD, friend.getId());

        Collection<Audit> feed = auditDbStorage.getEventsForUser(user.getId());
        Audit feedItem = feed.stream().findFirst().orElseThrow(() -> new RuntimeException("Аудит пуст"));

        assertEquals(1, feed.size());
        assertEquals(user.getId(), feedItem.getUserId());
        assertEquals(Events.FRIEND, feedItem.getEvent());
        assertEquals(Operations.ADD, feedItem.getOperation());
        assertEquals(friend.getId(), feedItem.getEntityId());
        assertTrue(feedItem.getTimestamp().isBefore(Instant.now())
                && feedItem.getTimestamp().isAfter(Instant.now().minusSeconds(10)));

    }

    @Test
    public void checkAddNewEvent_FriendRemove() {
        User user = userDbStorage.create(GenerateTestData.generateNewUser(List.of(3L)));
        User friend = userDbStorage.create(GenerateTestData.generateNewUser(List.of(4L)));

        auditDbStorage.addEvent(user.getId(), Events.FRIEND, Operations.ADD, friend.getId());
        auditDbStorage.addEvent(user.getId(), Events.FRIEND, Operations.REMOVE, friend.getId());

        Collection<Audit> feed = auditDbStorage.getEventsForUser(user.getId());
        Audit feedItem = feed.stream().filter(item -> item.getOperation().equals(Operations.REMOVE)).findFirst()
                .orElseThrow(() -> new RuntimeException("Нет события на удаление"));

        assertEquals(2, feed.size());
        assertEquals(user.getId(), feedItem.getUserId());
        assertEquals(Events.FRIEND, feedItem.getEvent());
        assertEquals(Operations.REMOVE, feedItem.getOperation());
        assertEquals(friend.getId(), feedItem.getEntityId());
        assertTrue(feedItem.getTimestamp().isBefore(Instant.now())
                && feedItem.getTimestamp().isAfter(Instant.now().minusSeconds(10)));

    }

    @Test
    public void checkAddNewEvent_LikeAdd() {
        User user = userDbStorage.create(GenerateTestData.generateNewUser(List.of(5L)));
        Film film = filmDbStorage.create(GenerateTestData.generateNewFilm(List.of(1L)));

        auditDbStorage.addEvent(user.getId(), Events.LIKE, Operations.ADD, film.getId());

        Collection<Audit> feed = auditDbStorage.getEventsForUser(user.getId());
        Audit feedItem = feed.stream().findFirst().orElseThrow(() -> new RuntimeException("Аудит пуст"));

        assertEquals(1, feed.size());
        assertEquals(user.getId(), feedItem.getUserId());
        assertEquals(Events.LIKE, feedItem.getEvent());
        assertEquals(Operations.ADD, feedItem.getOperation());
        assertEquals(film.getId(), feedItem.getEntityId());
        assertTrue(feedItem.getTimestamp().isBefore(Instant.now())
                && feedItem.getTimestamp().isAfter(Instant.now().minusSeconds(10)));

    }

    @Test
    public void checkAddNewEvent_LikeRemove() {
        User user = userDbStorage.create(GenerateTestData.generateNewUser(List.of(6L)));
        Film film = filmDbStorage.create(GenerateTestData.generateNewFilm(List.of(2L)));

        auditDbStorage.addEvent(user.getId(), Events.LIKE, Operations.ADD, film.getId());
        auditDbStorage.addEvent(user.getId(), Events.LIKE, Operations.REMOVE, film.getId());

        Collection<Audit> feed = auditDbStorage.getEventsForUser(user.getId());
        Audit feedItem = feed.stream().filter(item -> item.getOperation().equals(Operations.REMOVE)).findFirst()
                .orElseThrow(() -> new RuntimeException("Нет события на удаление"));

        assertEquals(2, feed.size());
        assertEquals(user.getId(), feedItem.getUserId());
        assertEquals(Events.LIKE, feedItem.getEvent());
        assertEquals(Operations.REMOVE, feedItem.getOperation());
        assertEquals(film.getId(), feedItem.getEntityId());
        assertTrue(feedItem.getTimestamp().isBefore(Instant.now())
                && feedItem.getTimestamp().isAfter(Instant.now().minusSeconds(10)));

    }

    @Test
    public void checkAddNewEvent_ReviewAdd() {
        User user = userDbStorage.create(GenerateTestData.generateNewUser(List.of(7L)));
        Film film = filmDbStorage.create(GenerateTestData.generateNewFilm(List.of(3L)));
        Review review = reviewDbStorage.create(GenerateTestData.generateNewReview(List.of(1L), user.getId(), film.getId(), true));

        auditDbStorage.addEvent(user.getId(), Events.REVIEW, Operations.ADD, review.getId());

        Collection<Audit> feed = auditDbStorage.getEventsForUser(user.getId());
        Audit feedItem = feed.stream().findFirst().orElseThrow(() -> new RuntimeException("Аудит пуст"));

        assertEquals(1, feed.size());
        assertEquals(user.getId(), feedItem.getUserId());
        assertEquals(Events.REVIEW, feedItem.getEvent());
        assertEquals(Operations.ADD, feedItem.getOperation());
        assertEquals(review.getId(), feedItem.getEntityId());
        assertTrue(feedItem.getTimestamp().isBefore(Instant.now())
                && feedItem.getTimestamp().isAfter(Instant.now().minusSeconds(10)));

    }

    @Test
    public void checkAddNewEvent_ReviewUpdate() {
        User user = userDbStorage.create(GenerateTestData.generateNewUser(List.of(9L)));
        Film film = filmDbStorage.create(GenerateTestData.generateNewFilm(List.of(5L)));
        Review review = reviewDbStorage.create(GenerateTestData.generateNewReview(List.of(3L), user.getId(), film.getId(), true));

        auditDbStorage.addEvent(user.getId(), Events.REVIEW, Operations.UPDATE, review.getId());

        Collection<Audit> feed = auditDbStorage.getEventsForUser(user.getId());
        Audit feedItem = feed.stream().findFirst().orElseThrow(() -> new RuntimeException("Аудит пуст"));

        assertEquals(1, feed.size());
        assertEquals(user.getId(), feedItem.getUserId());
        assertEquals(Events.REVIEW, feedItem.getEvent());
        assertEquals(Operations.UPDATE, feedItem.getOperation());
        assertEquals(review.getId(), feedItem.getEntityId());
        assertTrue(feedItem.getTimestamp().isBefore(Instant.now())
                && feedItem.getTimestamp().isAfter(Instant.now().minusSeconds(10)));

    }

    @Test
    public void checkAddNewEvent_ReviewRemove() {
        User user = userDbStorage.create(GenerateTestData.generateNewUser(List.of(8L)));
        Film film = filmDbStorage.create(GenerateTestData.generateNewFilm(List.of(4L)));
        Review review = reviewDbStorage.create(GenerateTestData.generateNewReview(List.of(2L), user.getId(), film.getId(), true));

        auditDbStorage.addEvent(user.getId(), Events.REVIEW, Operations.ADD, review.getId());
        auditDbStorage.addEvent(user.getId(), Events.REVIEW, Operations.REMOVE, review.getId());

        Collection<Audit> feed = auditDbStorage.getEventsForUser(user.getId());
        Audit feedItem = feed.stream().filter(item -> item.getOperation().equals(Operations.REMOVE)).findFirst()
                .orElseThrow(() -> new RuntimeException("Нет события на удаление"));

        assertEquals(2, feed.size());
        assertEquals(user.getId(), feedItem.getUserId());
        assertEquals(Events.REVIEW, feedItem.getEvent());
        assertEquals(Operations.REMOVE, feedItem.getOperation());
        assertEquals(review.getId(), feedItem.getEntityId());
        assertTrue(feedItem.getTimestamp().isBefore(Instant.now())
                && feedItem.getTimestamp().isAfter(Instant.now().minusSeconds(10)));

    }

}
