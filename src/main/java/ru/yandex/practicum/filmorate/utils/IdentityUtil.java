package ru.yandex.practicum.filmorate.utils;

import java.util.Collection;

public class IdentityUtil {
    public static Long getNextId(Collection<Long> ids) {
        long maxId = ids.stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0L);
        return ++maxId;
    }
}
