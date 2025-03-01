package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Random;

import static ru.yandex.practicum.filmorate.utils.IdentityUtil.getNextId;

public class GenerateTestData {

    public static User generateNewUser(Collection<Long> ids) {
        return generateNewUser(ids,
                10,
                20,
                10,
                LocalDate.of(1990, 1, 1)
        );
    }

    public static User generateNewUser(Collection<Long> ids, int emailLength, int nameLength, int loginLength, LocalDate birthday) {
        return User.builder()
                .id(getNextId(ids))
                .email(generateString(emailLength / 2) + "@" + generateString(emailLength / 2))
                .name(generateString(nameLength))
                .login(generateString(loginLength))
                .birthday(birthday)
                .build();

    }

    public static Film generateNewFilm(Collection<Long> ids) {
        return generateNewFilm(ids,
                10,
                30,
                LocalDate.of(2014, 1, 1),
                120
        );
    }

    public static Film generateNewFilm(Collection<Long> ids, int nameLength, int descLength, LocalDate date, int duration) {
        return Film.builder()
                .id(getNextId(ids))
                .name(generateString(nameLength))
                .description(generateString(descLength))
                .releaseDate(date)
                .duration(duration)
                .build();

    }

    public static String generateString(int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'

        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
