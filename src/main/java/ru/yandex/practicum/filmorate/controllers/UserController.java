package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    private static int id;

    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {
        log.debug("Получен запрос POST /users");
        if (validateUser(user)) {
            user.setId(++id);
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(id, user);
            log.debug("Пользователь с id = {} добавлен", id);
            return user;
        } else {
            log.error("Валидация не пройдена");
            throw new ValidationException();
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        log.debug("Получен запрос PUT /users");
        if (validateUser(user) && users.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.debug("Пользователь с id = {} обновлен", user.getId());
            return user;
        } else {
            log.error("Валидация не пройдена");
            throw new ValidationException();
        }
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.debug("Получен запрос GET /users");
        return users.values();
    }

    protected boolean validateUser(User user) {
        if (!user.getEmail().isBlank()
                && user.getEmail().contains("@")
                && !user.getLogin().isBlank()
                && !user.getLogin().contains(" ")
                && user.getBirthday().isBefore(LocalDate.now().plusDays(1))) {
            return true;
        } else {
            return false;
        }
    }
}
