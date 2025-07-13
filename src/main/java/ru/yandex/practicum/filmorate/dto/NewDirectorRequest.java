package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewDirectorRequest extends BaseDirectorRequest {

    private String name;

}

