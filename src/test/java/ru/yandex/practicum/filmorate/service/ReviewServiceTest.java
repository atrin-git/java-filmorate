package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewStorage reviewStorage;

    @InjectMocks
    private ReviewService reviewService;

    private Review review1;
    private Review review2;
    private Review review3;

    @BeforeEach
    void setUp() {
        review1 = review1.builder()
                .id(1L)
                .filmId(5L)
                .userId(6L)
                .isPositive(true)
                .content("Потрясающий фильм!")
                .useful(0L)
                .build();
        review2 = review2.builder()
                .id(2L)
                .filmId(10L)
                .userId(6L)
                .isPositive(true)
                .content("Хорошая комедия.")
                .useful(0L)
                .build();
        review3 = review3.builder()
                .id(3L)
                .filmId(10L)
                .userId(14L)
                .isPositive(false)
                .content("Плохой фильм.")
                .useful(0L)
                .build();
    }

    @Test
    void testGetReview_ById() {
        when(reviewStorage.find(1L)).thenReturn(Optional.of(review1));

        Review result = reviewService.find(1L);
        verify(reviewStorage).find(1L);
        assertEquals(review1, result);
    }

    @Test
    void testGetReview_ById_ShouldThrownNotFoundException() {
        when(reviewStorage.find(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.find(1L));
        verify(reviewStorage, times(1)).find(1L);
    }

    @Test
    void testDeleteReview_ById() {
        when(reviewStorage.find(1L)).thenReturn(Optional.of(review1));
        doNothing().when(reviewStorage).delete(1L);

        reviewService.delete(1L);
        verify(reviewStorage).delete(1L);
    }

    @Test
    void testDeleteReview_ById_ShouldThrownNotFoundException() {
        when(reviewStorage.find(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.delete(1L));
        verify(reviewStorage, never()).delete(1L);
    }

}
