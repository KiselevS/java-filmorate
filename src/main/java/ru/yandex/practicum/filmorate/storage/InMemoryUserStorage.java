package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private static Long id = 0L;

    @Override
    public User addUser(User user) {
        user.setId(++id);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(id, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        users.get(userId).getFriends().add(friendId);
        users.get(friendId).getFriends().add(userId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        users.get(userId).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(userId);
    }

    @Override
    public Set<Long> getFriends(Long userId) {
        return users.get(userId).getFriends();
    }

    @Override
    public Set<Long> getCommonFriends(Long userId, Long friendId) {
        Set<Long> commonFriends = new HashSet<>();
        for (Long id : users.get(userId).getFriends()) {
            if (users.get(friendId).getFriends().contains(id)) {
                commonFriends.add(id);
            }
        }
        return commonFriends;
    }
}
