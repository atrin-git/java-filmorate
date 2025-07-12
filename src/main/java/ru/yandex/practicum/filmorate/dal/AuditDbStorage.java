package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Component("db-audit")
public class AuditDbStorage extends BaseDbStorage<Audit> implements AuditStorage {

    private static final String QUERY_GET_ALL_EVENTS_FOR_USER = "SELECT * FROM audit WHERE user_id = ?";
    private static final String QUERY_ADD_NEW_EVENT = "INSERT INTO audit (timestamp, user_id, event_id, operation_id, entity_id) " +
            "VALUES (NOW(), ?, ?, ?, ?)";
    public AuditDbStorage(JdbcTemplate jdbc, RowMapper<Audit> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addEvent(Long userId, Events event, Operations operation, Long entityId) {
        insert(
                QUERY_ADD_NEW_EVENT,
                userId,
                event.getEventId(),
                operation.getOperationId(),
                entityId
        );
    }

    @Override
    public Collection<Audit> getEventsForUser(Long userId) {
        return findMany(
                QUERY_GET_ALL_EVENTS_FOR_USER,
                userId
        );
    }
}
