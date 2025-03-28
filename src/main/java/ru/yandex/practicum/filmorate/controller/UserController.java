package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final List<User> users = new ArrayList<>();
    private int idCounter = 1;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        user.setId(idCounter++);
        user.setName(user.getName());
        users.add(user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return users;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User updatedUser) {
        Optional<User> existingUser = users.stream()
                .filter(u -> u.getId() == updatedUser.getId())
                .findFirst();

        if (existingUser.isEmpty()) {
            throw new NotFoundException("Пользователь с ID " + updatedUser.getId() + " не найден.");
        }

        User user = existingUser.get();
        user.setEmail(updatedUser.getEmail());
        user.setLogin(updatedUser.getLogin());
        user.setName(updatedUser.getName());
        user.setBirthday(updatedUser.getBirthday());

        return user;
    }
}
