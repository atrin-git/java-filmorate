package ru.yandex.practicum.filmorate.dto.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.AuditDto;
import ru.yandex.practicum.filmorate.model.Audit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuditMapper {
    public static AuditDto mapToAuditDto(Audit audit) {
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
