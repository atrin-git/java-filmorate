package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
public class AuditDto {
    private Long eventId;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Instant timestamp;
    private Long userId;
    private String eventType;
    private String operation;
    private Long entityId;
}
