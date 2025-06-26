package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rates;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class UpdateFilmRequest extends BaseFilmRequest {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Rates mpa;
    private Set<Genre> genres;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasRates() {
        return mpa != null;
    }

    public boolean hasGenres() {
        return !(genres == null || genres.isEmpty());
    }

}
