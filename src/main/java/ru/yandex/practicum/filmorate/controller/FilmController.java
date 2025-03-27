package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exeption.ValidationException;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/films")
public class FilmController {

    private List<Film> films = new ArrayList<>();
    private int idCounter = 1;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@RequestBody @Valid Film film) {
        film.setId(idCounter++);
        films.add(film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return films;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if (film.getId() <= 0) {
            throw new ValidationException("ID фильма должен быть больше 0.");
        }

        Optional<Film> existingFilm = films.stream()
                .filter(f -> f.getId() == film.getId())
                .findFirst();

        if (existingFilm.isPresent()) {
            films.remove(existingFilm.get());
            films.add(film);
            return film;
        } else {
            throw new ValidationException("Фильм с ID " + film.getId() + " не найден.");
        }
    }
}
