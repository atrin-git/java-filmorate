package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.AuditDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
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

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable Long userId) {
        log.info("Получен запрос на получения пользователя с id: {}", userId);
        return userService.find(userId);
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

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        log.info("Получен запрос на удаление пользователя с id = {}", userId);
        userService.delete(userId);
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

    @GetMapping("/{id}/feed")
    public Collection<AuditDto> getFeed(@PathVariable("id") @NotNull Long userId) {
        log.info("Получен запрос на ленту событий пользователя с ID = {}", userId);
        return userService.getFeed(userId);
    }


    @GetMapping("{id}/recommendations")
    public Collection<FilmDto> getRecommendations(@PathVariable("id") Long userId) {
        log.info("Получен запрос на получение рекомендуемых фильмов пользователю {}", userId);
        return userService.getRecommendedFilms(userId);
    }

}
