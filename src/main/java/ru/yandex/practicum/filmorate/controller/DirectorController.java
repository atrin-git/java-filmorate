package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.dto.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;


@Slf4j
@RestController
@RequestMapping({"/directors"})
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectorDto create(@RequestBody NewDirectorRequest director) {
        log.info("Получен запрос на добавление режиссера");
        return this.directorService.create(director);
    }

    @PutMapping
    public DirectorDto update(@RequestBody UpdateDirectorRequest director) {
        log.info("Получен запрос на обновление режиссера с именем = {}", director.getName());
        return this.directorService.update(director);
    }

    @GetMapping
    public Collection<Director> findAll() {
        log.info("Получен запрос на получение всех режиссеров");
        return this.directorService.findAll();
    }

    @GetMapping({"/{id}"})
    public Director find(@PathVariable Long id) {
        log.info("Получен запрос на получение режиссера {}", id);
        return this.directorService.find(id);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("Получен запрос за удаление режиссера {}", id);
        this.directorService.delete(id);
        return ResponseEntity.ok().build();
    }
}
