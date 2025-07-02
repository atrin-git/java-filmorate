package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rates;
import ru.yandex.practicum.filmorate.service.RatesService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class RatesController {

    private final RatesService ratesService;

    @GetMapping
    public Collection<Rates> findAll() {
        log.info("Получен запрос за получение всех рейтингов");
        return ratesService.findAll();
    }

    @GetMapping("/{id}")
    public Rates find(@PathVariable Long id) {
        log.info("Получен запрос за получение данных о рейтинге {}", id);
        return ratesService.find(id);
    }

}
