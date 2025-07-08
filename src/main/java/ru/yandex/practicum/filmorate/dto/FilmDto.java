package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rates;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String description;
    private Rates mpa;
    private Collection<Genre> genres;
    private LocalDate releaseDate;
    private Integer duration;

    private Set<Long> likesByUsers;
}
