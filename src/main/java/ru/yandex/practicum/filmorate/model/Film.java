package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    private Long id;
    private String name;
    private String description;
    private Rates mpa;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Long> likesByUsers;
    private Collection<Genre> genres;
    private Collection<Director> directors;
}
