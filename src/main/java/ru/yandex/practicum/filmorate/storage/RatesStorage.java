package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Rates;

import java.util.Collection;
import java.util.Optional;

public interface RatesStorage {
    Collection<Rates> findAll();

    Optional<Rates> find(Long rateId);

    Optional<Rates> getFilmRating(long filmId);

}
