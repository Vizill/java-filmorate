package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int idCounter = 1;

    @Override
    public User create(User user) {
        user.setId(idCounter++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    @Override
    public List<User> getFriends(int userId) {
        User user = users.get(userId);
        if (user == null) {
            return List.of();
        }
        return user.getFriends().stream()
                .map(users::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        User user = users.get(userId);
        User otherUser = users.get(otherId);

        if (user == null || otherUser == null) {
            return List.of();
        }

        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(users::get)
                .filter(Objects::nonNull)
                .toList();
    }
}