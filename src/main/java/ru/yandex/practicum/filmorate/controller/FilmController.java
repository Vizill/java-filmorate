package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private int idCounter = 1;

    @PostMapping
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
    public Film updateFilm(@RequestBody @Valid Film updatedFilm) {
        Optional<Film> existingFilm = films.stream()
                .filter(f -> f.getId() == updatedFilm.getId())
                .findFirst();

        if (existingFilm.isEmpty()) {
            throw new NotFoundException("Фильм с ID " + updatedFilm.getId() + " не найден.");
        }

        Film film = existingFilm.get();
        film.setName(updatedFilm.getName());
        film.setDescription(updatedFilm.getDescription());
        film.setReleaseDate(updatedFilm.getReleaseDate());
        film.setDuration(updatedFilm.getDuration());

        return film;
    }
}
