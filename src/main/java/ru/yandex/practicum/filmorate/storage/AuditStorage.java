package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Audit;
import ru.yandex.practicum.filmorate.model.Events;
import ru.yandex.practicum.filmorate.model.Operations;

import java.util.Collection;

public interface AuditStorage {
    void addEvent(Long userId, Events event, Operations operation, Long entityId);

    Collection<Audit> getEventsForUser(Long userId);
}
