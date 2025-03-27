package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final List<User> users = new ArrayList<>();
    private int idCounter = 1;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        user.setId(idCounter++);

        // Если имя пустое, то устанавливаем его равным логину
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(users);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody @Valid User user) {
        if (user.getId() <= 0) {
            throw new ValidationException("ID пользователя должен быть больше 0.");
        }

        for (User u : users) {
            if (u.getId() == user.getId()) {
                u.setEmail(user.getEmail());
                u.setLogin(user.getLogin());
                u.setName(user.getName().isBlank() ? user.getLogin() : user.getName());
                u.setBirthday(user.getBirthday());
                return ResponseEntity.ok(u);
            }
        }

        throw new ValidationException("Пользователь с ID " + user.getId() + " не найден.");
    }
}
