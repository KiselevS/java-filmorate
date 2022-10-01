package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public List<User> getUsers() {
        return userStorage.getUsers();
    }


    public Optional<User> getUserById(long userId) {
        final Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException();
        }
        return user;
    }


    public User addUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }


    public User updateUser(User user) {
        if (userStorage.getUserById(user.getId()).isEmpty()) {
            throw new NotFoundException();
        }
        return userStorage.updateUser(user);
    }


    public void addFriend(long userId, long friendId) {
        if (userId < 1 || friendId < 1) {
            throw new NotFoundException();
        }
        userStorage.addFriend(userId, friendId);
    }


    public void removeFriend(long userId, long friendId) {
        Optional<User> user = getUserById(userId);
        Optional<User> friend = getUserById(friendId);
        if (user.isPresent() & friend.isPresent()) {
            userStorage.removeFriend(userId, friendId);
        }
    }


    public Collection<User> getUserFriends(long userId) {
        if (userId < 1) {
            throw new NotFoundException();
        }
        return userStorage.getUserFriends(userId);
    }


    public List<User> getCommonFriends(long userId, long otherUserId) {
        if (userStorage.getUserById(userId).isEmpty() || userStorage.getUserById(otherUserId).isEmpty()) {
            throw new NotFoundException();
        }
        return userStorage.getCommonFriends(userId, otherUserId);
    }
}
