package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
public class AuditDto {
    private Long eventId;
    private Instant timestamp;
    private Long userId;
    private String eventType;
    private String operation;
    private Long entityId;
}
