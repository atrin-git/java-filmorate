package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Set;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

public interface DirectorsFilmsStorage {

    void setDirectorsForFilm(long id, Set<Director> directors);

    Collection<Film> getDirectorOnFilm(Long id);

    Collection<Director> getDirectorsForFilm(Long id);
}
