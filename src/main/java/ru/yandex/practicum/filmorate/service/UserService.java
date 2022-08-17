package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (validateUser(user)) {
            log.debug("Пользователь с id = {} добавлен", user.getId());
            return userStorage.addUser(user);
        } else {
            log.error("Валидация не пройдена");
            throw new ValidationException();
        }
    }

    public User updateUser(User user) {
        if (userStorage.getUserById(user.getId()) != null) {
            if (validateUser(user)) {
                log.debug("Пользователь с id = {} обновлен", user.getId());
                return userStorage.updateUser(user);
            } else {
                log.error("Валидация не пройдена");
                throw new ValidationException();
            }
        } else {
            throw new NotFoundException();
        }

    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    private boolean validateUser(User user) {
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

    public User getUserById(Long id) {
        final User user = userStorage.getUserById(id);
        if (user != null) {
            return user;
        } else {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
    }

    public void addFriend(Long id, Long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        if (user != null && friend != null) {
            userStorage.addFriend(id, friendId);
        } else {
            throw new NotFoundException();
        }
    }

    public void removeFriend(Long id, Long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        if (user != null && friend != null) {
            userStorage.removeFriend(id, friendId);
        } else {
            throw new NotFoundException();
        }
    }

    public Set<User> getFriends(Long id) {
        Set<User> friends = new HashSet<>();
        final User user = userStorage.getUserById(id);
        if (user != null) {
            for (Long friendId : user.getFriends()){
                friends.add(userStorage.getUserById(friendId));
            }
            return friends;
        } else {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
    }

    public Set<User> getCommonFriends(Long id, Long otherId) {
        Set<User> commonFriends = new HashSet<>();
        final User user = userStorage.getUserById(id);
        final User otherUser = userStorage.getUserById(otherId);

        if (user != null && otherUser != null) {
            for (Long friendId : user.getFriends()) {
                if (otherUser.getFriends().contains(friendId)) {
                    commonFriends.add(userStorage.getUserById(friendId));
                }
            }
            return commonFriends;
        } else {
            throw new NotFoundException();
        }
    }
}
