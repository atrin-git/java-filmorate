package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rates;
import ru.yandex.practicum.filmorate.storage.RatesStorage;

import java.util.Collection;

@Slf4j
@Service
public class RatesService {

    @Autowired
    @Qualifier("db-rates")
    private RatesStorage ratesStorage;

    public Collection<Rates> findAll() {
        return ratesStorage.findAll().stream()
                .toList();
    }

    public Rates find(Long id) {
        return ratesStorage.find(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден с ID: " + id));
    }

}
