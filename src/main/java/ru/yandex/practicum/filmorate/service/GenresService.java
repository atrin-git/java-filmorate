package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresStorage;

import java.util.Collection;

@Slf4j
@Service
public class GenresService {

    @Autowired
    @Qualifier("db-genres")
    private GenresStorage genresStorage;

    public Collection<Genre> findAll() {
        return genresStorage.findAll().stream()
                .toList();
    }

    public Genre find(Long id) {
        return genresStorage.find(id)
                .orElseThrow(() -> new NotFoundException("Жанр не найден с ID: " + id));
    }

}
