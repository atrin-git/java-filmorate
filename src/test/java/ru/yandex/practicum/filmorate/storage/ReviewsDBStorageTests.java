package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rates;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewsDBStorageTests {

    private final ReviewStorage reviewStorage;
    private final ReviewRatingStorage reviewRatingStorage;
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    private Review review1;
    private Film film1;
    private User user1;

    @BeforeEach
    public void setUp() {
        film1 = filmStorage.create(Film.builder()
                .name("Фильм 1")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .likesByUsers(Set.of(1L, 2L, 3L))
                .rating(Rates.builder().id(1L).build())
                .genres(Set.of(Genre.builder().id(1L).build()))
                .build()
        );
        user1 = userStorage.create(User.builder()
                .email("user1@yandex.ru")
                .login("ivan111")
                .name("Иван")
                .password("qwerty1")
                .birthday(LocalDate.of(1997, 1, 21))
                .build()
        );
        review1 = review1.builder()
                .filmId(film1.getId())
                .userId(user1.getId())
                .isPositive(true)
                .content("Потрясающий фильм!")
                .useful(0L)
                .build();
    }

    @Test
    public void testCreateReview() {
        Review createdReview = reviewStorage.create(review1);

        assertNotNull(createdReview.getId(), "ID отзыва не должен быть null");
        assertEquals("Потрясающий фильм!", createdReview.getContent(), "Содержание отзыва не совпадает");
        assertTrue(createdReview.getIsPositive(), "Флаг isPositive должен быть true");
        assertEquals(0L, createdReview.getUseful(), "Полезность должна быть 0 для нового отзыва");
    }

    @Test
    public void testUpdateReview() {
        Review createdReview = reviewStorage.create(review1);
        Review updatedReview = Review.builder()
                .id(createdReview.getId())
                .filmId(film1.getId())
                .userId(user1.getId())
                .isPositive(false)
                .content("Пересмотрел мнение")
                .build();

        reviewStorage.update(updatedReview);

        Optional<Review> updatedFoundReview = reviewStorage.find(createdReview.getId());
        assertTrue(updatedFoundReview.isPresent(), "Обновленный отзыв должен быть найден");
        assertEquals("Пересмотрел мнение", updatedFoundReview.get().getContent(), "Содержание не обновилось");
        assertFalse(updatedFoundReview.get().getIsPositive(), "Флаг isPositive должен быть false");
    }

    @Test
    public void testFindLimitedReviews_ById() {
        User user2 = userStorage.create(
                User.builder()
                        .id(2L)
                        .email("user2@yandex.ru")
                        .login("ekaterina222")
                        .name("Екатерина")
                        .password("qwerty2")
                        .birthday(LocalDate.of(1985, 4, 5))
                        .build()
        );

        Review review2 = Review.builder()
                .filmId(film1.getId())
                .userId(user2.getId())
                .isPositive(true)
                .content("Потрясающий фильм!")
                .useful(0L)
                .build();

        review1 = reviewStorage.create(review1);
        review2 = reviewStorage.create(review2);

        Collection<Review> filmReviews = reviewStorage.findLimitedReviews(film1.getId(), 10L);
        assertEquals(2, filmReviews.size(), "Должен вернуться 1 отзыв для фильма");
    }

    @Test
    public void testAddLikeOnReview() {
        review1 = reviewStorage.create(review1);
        reviewRatingStorage.addRatingOnReview(user1.getId(), review1.getId(), true);

        assertEquals(1, reviewStorage.find(review1.getId()).orElseThrow().getUseful());
    }

    @Test
    public void testDeleteLikeOnReview() {
        review1 = reviewStorage.create(review1);
        reviewRatingStorage.addRatingOnReview(user1.getId(), review1.getId(), true);
        reviewRatingStorage.removeRatingOnReview(review1.getId(), user1.getId());

        assertEquals(0, reviewStorage.find(review1.getId()).orElseThrow().getUseful());
    }

    @Test
    public void testAddDislikeOnReview() {
        review1 = reviewStorage.create(review1);
        reviewRatingStorage.addRatingOnReview(user1.getId(), review1.getId(), false);

        assertEquals(-1, reviewStorage.find(review1.getId()).orElseThrow().getUseful());
    }

    @Test
    public void testDeleteDislikeOnReview() {
        review1 = reviewStorage.create(review1);
        reviewRatingStorage.addRatingOnReview(user1.getId(), review1.getId(), false);
        reviewRatingStorage.removeRatingOnReview(review1.getId(), user1.getId());

        assertEquals(0, reviewStorage.find(review1.getId()).orElseThrow().getUseful());
    }

}
