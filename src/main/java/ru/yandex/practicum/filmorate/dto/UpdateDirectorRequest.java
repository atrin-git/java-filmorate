package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UpdateDirectorRequest extends BaseDirectorRequest {
    private Long id;
    private String name;
}

