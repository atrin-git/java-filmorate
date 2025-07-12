package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class NewDirectorRequest extends BaseDirectorRequest {

    private String name;

    public NewDirectorRequest() {
    }

    public NewDirectorRequest(String name) {
        this.name = name;
    }
}

