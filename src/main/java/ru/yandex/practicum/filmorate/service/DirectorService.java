package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.DirectorDbStorage;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.dto.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.dto.mappers.DirectorMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.model.validation.DirectorValidator.checkDirectorIsValid;

@Slf4j
@Service
public class DirectorService {
    @Autowired
    private DirectorDbStorage directorStorage;

    public Collection<DirectorDto> findAll() {
        return directorStorage.findAll().stream().map(DirectorMapper::mapToDirectorDto).toList();
    }

    public DirectorDto find(Long id) {
        Director result = directorStorage.find(id).orElseThrow(() -> new NotFoundException("Режиссер не найден с ID: " + id));
        return DirectorMapper.mapToDirectorDto(result);
    }

    public DirectorDto create(NewDirectorRequest newDirector) {
        Director director = DirectorMapper.mapToDirector(newDirector);
        checkDirectorIsValid(director);
        directorStorage.create(director);
        return DirectorMapper.mapToDirectorDto(director);
    }

    public DirectorDto update(UpdateDirectorRequest updateDirector) {
        Director director = DirectorMapper.mapToDirector(updateDirector);
        checkDirectorIsValid(director);
        directorStorage.find(updateDirector.getId()).orElseThrow(() -> {
            log.warn("Не найден режиссер с ID = {}", updateDirector.getId());
            return new NotFoundException("режиссер не найден с ID: " + updateDirector.getId());
        });
        return DirectorMapper.mapToDirectorDto(directorStorage.update(director));
    }

    public void delete(Long id) {
        directorStorage.find(id).orElseThrow(() -> {
            log.warn("Не найден режиссер с ID = {}", id);
            return new NotFoundException("режиссер не найден с ID: " + id);
        });
        this.directorStorage.delete(id);
    }
}
