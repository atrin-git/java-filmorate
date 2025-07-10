package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Set;
import ru.yandex.practicum.filmorate.model.Director;

public interface DirectorsFilmsStorage {

    void setDirectorsForFilm(long id, Set<Director> directors);

    Collection<Director> getDirectorOnFilm(Long id);
}
