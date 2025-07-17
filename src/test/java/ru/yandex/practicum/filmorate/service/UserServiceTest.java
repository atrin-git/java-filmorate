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

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        user1 = User.builder()
                .id(1L)
                .email("user1@yandex.ru")
                .login("ivan111")
                .name("Иван")
                .password("qwerty1")
                .birthday(LocalDate.of(1997, 1, 21))
                .build();
        user2 = User.builder()
                .id(2L)
                .email("user2@yandex.ru")
                .login("ekaterina222")
                .name("Екатерина")
                .password("qwerty2")
                .birthday(LocalDate.of(1985, 4, 5))
                .build();
        user3 = User.builder()
                .id(3L)
                .email("user3@yandex.ru")
                .login("sergey333")
                .name("Сергей")
                .password("qwerty3")
                .birthday(LocalDate.of(2003, 6, 25))
                .build();
    }

    @Test
    public void testDeleteUser_ById() {
        when(userStorage.find(1L)).thenReturn(Optional.of(user1));
        doNothing().when(userStorage).delete(1L);
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
        when(userStorage.find(1L)).thenReturn(Optional.of(user1));
        UserDto user1Dto = UserMapper.mapToUserDto(user1);

        assertEquals(user1Dto, userService.find(1L));
    }
}