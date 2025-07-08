package ru.yandex.practicum.filmorate.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class Utils {
    public static Long getNextId(Collection<Long> ids) {
        long maxId = ids.stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0L);
        return ++maxId;
    }

    public static <T> void clearStringData(T obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Arrays.stream(fields)
                .filter(field -> field.getType().equals(String.class))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        if (value != null) {
                            field.set(obj, value.toString().trim());
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
