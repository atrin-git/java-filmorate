package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.utils.Utils.clearStringData;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> findAll() {
        log.info("Получен запрос на получение данных о всех пользователях");
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody NewUserRequest user) {
        log.info("Получен запрос на добавление пользователя");
        clearStringData(user);
        return userService.create(user);
    }

    @PutMapping
    public UserDto update(@RequestBody UpdateUserRequest user) {
        log.info("Получен запрос на обновление пользователя с id = {}", user.getId());
        clearStringData(user);
        return userService.update(user);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDto> getFriends(@PathVariable("id") Long userId) {
        log.info("Получен запрос за получение списка друзей пользователя {}", userId);
        return userService.getFriends(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        log.info("Получен запрос за добавление в друзья к {} пользователя {}", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        log.info("Получен запрос за удаление из друзей {} пользователя {}", userId, friendId);
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDto> findCommonFriends(@PathVariable("id") Long userId, @PathVariable("otherId") Long friendId) {
        log.info("Получен запрос за нахождение общих друзей у {} и {}", userId, friendId);
        return userService.findCommonFriends(userId, friendId);
    }


}
