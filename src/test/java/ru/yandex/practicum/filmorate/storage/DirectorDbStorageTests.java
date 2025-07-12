package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.dal.mappers.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.utils.GenerateTestData;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, GenresDbStorage.class, LikesDbStorage.class, RatesDbStorage.class,
        FilmRowMapper.class, GenresRowMapper.class, LikesRowMapper.class, RatesRowMapper.class,
        DirectorDbStorage.class, DirectorsFilmsDbStorage.class, DirectorsRowMapper.class})
public class DirectorDbStorageTests {
    private final DirectorDbStorage directorDbStorage;

    @Test
    public void checkCreateAndFindDirectorById() {
        Director director = directorDbStorage.create(GenerateTestData.generateNewDirector(List.of(1L),10));
        Optional<Director> directorOptional = directorDbStorage.find(director.getId());

        assertThat(directorOptional)
                .isPresent()
                .hasValueSatisfying(dir ->
                        assertThat(dir).hasFieldOrPropertyWithValue("id", director.getId())
                );
    }

    @Test
    public void checkFindAllFilms() {
        directorDbStorage.create(GenerateTestData.generateNewDirector(List.of(1L),10));
        directorDbStorage.create(GenerateTestData.generateNewDirector(List.of(1L),10));
        directorDbStorage.create(GenerateTestData.generateNewDirector(List.of(1L),10));
        Collection<Director> films = directorDbStorage.findAll();
        assertEquals(3, films.size(), "Вернулись не все режиссеры");
    }

    @Test
    public void checkDeleteFilm() {
        Director director = directorDbStorage.create(GenerateTestData.generateNewDirector(List.of(1L),10));
        directorDbStorage.delete(director.getId());
        Optional<Director> deletedFilm = directorDbStorage.find(director.getId());

        assertEquals(Optional.empty(), deletedFilm, "Удалённый Режиссер не должен был быть найден");
    }

    @Test
    public void checkUpdateFilm() {
        Director director = directorDbStorage.create(GenerateTestData.generateNewDirector(List.of(1L),10));
        Director directorCreated = directorDbStorage.create(director);
        Director updateDirector = GenerateTestData.generateNewDirector(List.of(2L),10);
        updateDirector.setId(directorCreated.getId());
        directorDbStorage.update(updateDirector);

        Optional<Director> actualFilm = directorDbStorage.find(directorCreated.getId());
        assertThat(actualFilm)
                .isPresent()
                .hasValueSatisfying(value -> assertEquals(updateDirector, value));
    }
}
