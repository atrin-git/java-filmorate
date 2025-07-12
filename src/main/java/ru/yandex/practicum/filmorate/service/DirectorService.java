package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.dto.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.dto.mappers.DirectorMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

@Slf4j
@Service
public class DirectorService {
    @Autowired
    @Qualifier("db-directors")
    private DirectorStorage directorStorage;

    public Collection<Director> findAll() {
        return directorStorage.findAll().stream().toList();
    }

    public Director find(Long id) {
        return directorStorage.find(id).orElseThrow(() -> {
            return new NotFoundException("Режиссер не найден с ID: " + id);
        });
    }

    public DirectorDto create(NewDirectorRequest director) {
        Director result = DirectorMapper.mapToDirector(director);
        this.directorStorage.create(result);
        return DirectorMapper.mapToDirectorDto(result);
    }

    public DirectorDto update(UpdateDirectorRequest director) {
        Director result = DirectorMapper.mapToDirector(director);

        if (!director.hasName()) {
            return DirectorMapper.mapToDirectorDto(result);
        } else {
            directorStorage.find(director.getId()).orElseThrow(() -> {
                log.warn("Не найден режиссер с ID = {}", director.getId());
                return new NotFoundException("режиссер не найден с ID: " + director.getId());
            });
            return DirectorMapper.mapToDirectorDto(directorStorage.update(result));
        }
    }

    public void delete(Long id) {
        this.directorStorage.find(id).orElseThrow(() -> {
            log.warn("Не найден режиссер с ID = {}", id);
            return new NotFoundException("режиссер не найден с ID: " + id);
        });
        this.directorStorage.delete(id);
    }
}
