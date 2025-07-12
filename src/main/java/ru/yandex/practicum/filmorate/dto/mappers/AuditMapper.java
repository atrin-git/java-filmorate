package ru.yandex.practicum.filmorate.dto.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.AuditDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Audit;
import ru.yandex.practicum.filmorate.model.Events;
import ru.yandex.practicum.filmorate.model.Operations;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuditMapper {
    public static AuditDto mapToAuditDto(Audit audit) {
//        String eventType = Arrays.stream(Events.values())
//                .filter(e -> e.getEventId().equals(audit.getEvent().))
//                .findFirst()
//                .orElseThrow(() -> new NotFoundException("Не найден тип события с id = " + audit.getEventId()))
//                .toString();
//
//        String operation = Arrays.stream(Operations.values())
//                .filter(e -> e.getOperationId().equals(audit.getOperationId()))
//                .findFirst()
//                .orElseThrow(() -> new NotFoundException("Не найден тип операции с id = " + audit.getOperationId()))
//                .toString();

        return AuditDto.builder()
                .eventId(audit.getId())
                .timestamp(audit.getTimestamp())
                .userId(audit.getUserId())
                .eventType(audit.getEvent().toString())
                .operation(audit.getOperation().toString())
                .entityId(audit.getEntityId())
                .build();
    }
}
