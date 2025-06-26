package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class NewUserRequest extends BaseUserRequest {
    private String name;
    private String login;
    private String email;
    private String password;
    private LocalDate birthday;
}
