package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Likes;

import java.util.Collection;

public interface LikesStorage {
    Collection<Likes> getLikesOnFilm(long filmId);

    void addLikeOnFilm(Long filmId, Long userId);

    void removeLikeOnFilm(Long filmId, Long userId);

}
