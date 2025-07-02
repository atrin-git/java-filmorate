package ru.yandex.practicum.filmorate.dto.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public static User mapToUser(NewUserRequest request) {
        return User.builder()
                .name(request.getName())
                .login(request.getLogin())
                .email(request.getEmail())
                .password(request.getPassword())
                .birthday(request.getBirthday())
                .build();
    }

    public static User mapToUser(UpdateUserRequest request) {
        return User.builder()
                .id(request.getId())
                .name(request.getName())
                .login(request.getLogin())
                .email(request.getEmail())
                .password(request.getPassword())
                .birthday(request.getBirthday())
                .build();
    }

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .friends(user.getFriends())
                .build();
    }

    public static User updateUserFields(User user, UpdateUserRequest request) {
        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasLogin()) {
            user.setLogin(request.getLogin());
        }
        if (request.hasPassword()) {
            user.setPassword(request.getPassword());
        }
        if (request.hasName()) {
            user.setName(request.getName());
        }
        if (request.hasBirthday()) {
            user.setBirthday(request.getBirthday());
        }
        return user;
    }
}