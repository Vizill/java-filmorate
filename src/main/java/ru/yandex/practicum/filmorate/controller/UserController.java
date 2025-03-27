package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final List<User> users = new ArrayList<>();
    private int idCounter = 1;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
        validateUser(user);
        user.setId(idCounter++);

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
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user) {
        validateUser(user);

        for (User u : users) {
            if (u.getId() == user.getId()) {
                u.setEmail(user.getEmail());
                u.setLogin(user.getLogin());
                u.setName(user.getName().isBlank() ? user.getLogin() : user.getName());
                u.setBirthday(user.getBirthday());
                return ResponseEntity.ok(u);
            }
        }

        throw new NotFoundException("Пользователь с ID " + user.getId() + " не найден.");
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный email: " + user.getEmail());
        }
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
