package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Audit;
import ru.yandex.practicum.filmorate.model.Events;
import ru.yandex.practicum.filmorate.model.Operations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@Component
public class AuditRowMapper implements RowMapper<Audit> {
    @Override
    public Audit mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Integer eventId = resultSet.getInt("event_id");
        Events event = Arrays.stream(Events.values())
                .filter(e -> e.getEventId().equals(eventId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Не найден тип события с id = " + eventId));

        Integer operationId = resultSet.getInt("operation_id");
        Operations operation = Arrays.stream(Operations.values())
                .filter(e -> e.getOperationId().equals(operationId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Не найден тип операции с id = " + eventId));

        return Audit.builder()
                .id(resultSet.getLong("id"))
                .timestamp(resultSet.getTimestamp("timestamp").toInstant())
                .userId(resultSet.getLong("user_id"))
                .event(event)
                .operation(operation)
                .entityId(resultSet.getLong("entity_id"))
                .build();
    }

}
