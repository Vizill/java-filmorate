package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
}
