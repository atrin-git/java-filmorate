package ru.yandex.practicum.filmorate.model.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DirectorValidator {

    public static void checkDirectorIsValid(Director director) {
        if (director.getName() == null || director.getName().trim().isEmpty()) {
            log.debug("Передано значение name = {}. Валидация не пройдена", director.getName());
            throw new ValidationException("Имя директора не может быть пустым");
        }
    }

}
