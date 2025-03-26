package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private List<User> users = new ArrayList<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Создан пользователь: {}", user.getLogin());
        users.add(user);
        return user;
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @Valid @RequestBody User user) {
        log.info("Обновлен пользователь с id: {}", id);
        User updatedUser = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ValidationException("Пользователь не найден"));
        updatedUser.setEmail(user.getEmail());
        updatedUser.setLogin(user.getLogin());
        updatedUser.setName(user.getName());
        updatedUser.setBirthday(user.getBirthday());
        return updatedUser;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }
}
