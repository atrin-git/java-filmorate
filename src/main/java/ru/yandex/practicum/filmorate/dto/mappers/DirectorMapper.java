package ru.yandex.practicum.filmorate.dto.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.dto.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.model.Director;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DirectorMapper {
    public static Director mapToDirector(NewDirectorRequest director) {
        return Director.builder().name(director.getName()).build();
    }

    public static Director mapToDirector(UpdateDirectorRequest director) {
        return Director.builder().name(director.getName()).id(director.getId()).build();
    }

    public static DirectorDto mapToDirectorDto(Director director) {
        return DirectorDto.builder().id(director.getId()).name(director.getName()).build();
    }
}
