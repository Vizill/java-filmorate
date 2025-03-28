package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
        user.setId(idCounter++);
        user.setName(user.getName() == null || user.getName().isBlank() ? user.getLogin() : user.getName());
        users.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(users);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user) {
        Optional<User> existingUser = users.stream()
                .filter(u -> u.getId() == user.getId())
                .findFirst();

        if (existingUser.isEmpty()) {
            throw new NotFoundException("Пользователь с ID " + user.getId() + " не найден.");
        }

        User updatedUser = existingUser.get();
        updatedUser.setEmail(user.getEmail());
        updatedUser.setLogin(user.getLogin());
        updatedUser.setName(user.getName().isBlank() ? user.getLogin() : user.getName());
        updatedUser.setBirthday(user.getBirthday());

        return ResponseEntity.ok(updatedUser);
    }
}