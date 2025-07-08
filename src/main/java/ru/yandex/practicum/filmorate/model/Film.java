package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    private Long id;
    private String name;
    private String description;
    private Rates rating;
    private LocalDate releaseDate;
    private Integer duration;

    private Set<Long> likesByUsers;
    private Set<Genre> genres;
}
