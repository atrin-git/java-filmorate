package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class UpdateUserRequest extends BaseUserRequest {
    private long id;
    private String name;
    private String login;
    private String email;
    private String password;
    private LocalDate birthday;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasLogin() {
        return !(login == null || login.isBlank());
    }

    public boolean hasEmail() {
        return !(email == null || email.isBlank());
    }

    public boolean hasPassword() {
        return !(password == null || password.isBlank());
    }

    public boolean hasBirthday() {
        return birthday != null;
    }
}
