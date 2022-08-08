package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {
    private final UserController userController = new UserController();

    @Test
    void emailTest() {
        User user1 = new User(1, "", "login", "name", LocalDate.of(1989, 6, 13));
        User user2 = new User(2, "email", "login", "name", LocalDate.of(1989, 6, 13));

        assertFalse(userController.validateUser(user1), "Не пройден тест на пустой email");
        assertFalse(userController.validateUser(user2), "Не пройден тест на email без @");
    }

    @Test
    void loginTest() {
        User user1 = new User(1, "email@ru.ru", "", "name", LocalDate.of(1989, 6, 13));
        User user2 = new User(2, "email@ru.ru", "lo gin", "name", LocalDate.of(1989, 6, 13));

        assertFalse(userController.validateUser(user1), "Не пройден тест на пустой логин");
        assertFalse(userController.validateUser(user2), "Не пройден тест на логин с пробелом");
    }

    @Test
    void nameTest() {
        User user = new User(1, "email@ru.ru", "login", "", LocalDate.of(1989, 6, 13));
        assertTrue(userController.validateUser(user));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
        assertEquals(user.getLogin(), user.getName(), "Пустое имя не заменено на логин");
    }

    @Test
    void dateOfBirthTest() {
        User user1 = new User(1, "email@ru.ru", "login", "name", LocalDate.now());
        User user2 = new User(2, "email@ru.ru", "login", "name", LocalDate.now().plusDays(1));

        assertTrue(userController.validateUser(user1));
        assertFalse(userController.validateUser(user2), "Не пройден тест на дату рождения");
    }

}