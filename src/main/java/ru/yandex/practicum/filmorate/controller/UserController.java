package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exeption.ValidationException;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private List<User> users = new ArrayList<>();
    private int idCounter = 1;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid User user) {
        user.setId(idCounter++);
        users.add(user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return users;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        if (user.getId() <= 0) {
            throw new ValidationException("ID пользователя должен быть больше 0.");
        }

        Optional<User> existingUser = users.stream()
                .filter(u -> u.getId() == user.getId())
                .findFirst();

        if (existingUser.isPresent()) {
            users.remove(existingUser.get());
            users.add(user);
            return user;
        } else {
            throw new ValidationException("Пользователь с ID " + user.getId() + " не найден.");
        }
    }
}
