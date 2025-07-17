package ru.yandex.practicum.filmorate.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.mappers.UserMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserStorage userStorage;
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .email("user1@yandex.ru")
                .login("ivan111")
                .name("Иван")
                .password("qwerty1")
                .birthday(LocalDate.of(1997, 1, 21))
                .build();
    }

    @Test
    public void testDeleteUser_ById() {
        when(userStorage.find(1L)).thenReturn(Optional.of(user));
        userService.delete(1L);

        verify(userStorage).delete(1L);
    }

    @Test
    public void testDeleteUser_ById_ShouldThrownNotFoundException() {
        when(userStorage.find(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,  () -> userService.delete(1L));
        verify(userStorage, never()).delete(1L);
    }

    @Test
    public void testGetUser_ById() {
        when(userStorage.find(1L)).thenReturn(Optional.of(user));
        UserDto user1Dto = UserMapper.mapToUserDto(user);

        assertEquals(user1Dto, userService.find(1L));
    }
}