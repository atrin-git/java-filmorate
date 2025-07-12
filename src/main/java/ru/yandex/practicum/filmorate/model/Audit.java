package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
public class Audit {
    private Long id;
    private Instant timestamp;
    private Long userId;
    private Integer eventId;
    private Integer operationId;
    private Long entityId;
}
