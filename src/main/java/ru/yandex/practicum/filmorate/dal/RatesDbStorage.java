package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rates;
import ru.yandex.practicum.filmorate.storage.RatesStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component("db-rates")
public class RatesDbStorage extends BaseDbStorage<Rates> implements RatesStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM ratings";
    private static final String FIND_QUERY = "SELECT * FROM ratings WHERE id = ?";
    private static final String FIND_LIKES_ON_FILM = "SELECT * FROM ratings " +
            "WHERE id IN (SELECT rating_id FROM films WHERE id = ?)";

    public RatesDbStorage(JdbcTemplate jdbc, RowMapper<Rates> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Rates> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Rates> find(Long rateId) {
        return findOne(FIND_QUERY,
                rateId);
    }

    @Override
    public Optional<Rates> getFilmRating(long filmId) {
        return findOne(
                FIND_LIKES_ON_FILM,
                filmId
        );
    }
}


