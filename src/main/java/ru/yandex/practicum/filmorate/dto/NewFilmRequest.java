package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rates;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class NewFilmRequest extends BaseFilmRequest {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Rates mpa;
    private Set<Genre> genres;
}
