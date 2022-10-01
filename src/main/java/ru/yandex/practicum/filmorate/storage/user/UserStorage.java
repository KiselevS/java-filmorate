package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    Optional<User> getUserById(long userId);

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    Collection<User> getUserFriends(long userId);

    List<User> getCommonFriends(long userId, long otherUserId);
}
