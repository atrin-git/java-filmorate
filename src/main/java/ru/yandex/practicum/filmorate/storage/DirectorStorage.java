package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.Director;

public interface DirectorStorage {

    Director create(Director director);

    Collection<Director> findAll();

    Optional<Director> find(Long id);

    Director update(Director result);

    void delete(Long id);
}

