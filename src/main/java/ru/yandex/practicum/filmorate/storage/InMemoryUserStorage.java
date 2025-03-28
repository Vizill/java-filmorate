package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Set<Integer>> friends = new HashMap<>();
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
        friends.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        friends.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        Optional.ofNullable(friends.get(userId)).ifPresent(set -> set.remove(friendId));
        Optional.ofNullable(friends.get(friendId)).ifPresent(set -> set.remove(userId));
    }

    @Override
    public List<User> getFriends(int userId) {
        return Optional.ofNullable(friends.get(userId))
                .map(ids -> ids.stream()
                        .map(users::get)
                        .filter(Objects::nonNull)
                        .toList())
                .orElse(List.of());
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        Set<Integer> userFriends = friends.getOrDefault(userId, Set.of());
        Set<Integer> otherFriends = friends.getOrDefault(otherId, Set.of());
        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(users::get)
                .filter(Objects::nonNull)
                .toList();
    }
}