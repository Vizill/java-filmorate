package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private List<Film> films = new ArrayList<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Добавлен фильм: {}", film.getName());
        films.add(film);
        return film;
    }

    @PutMapping("/{id}")
    public Film updateFilm(@PathVariable int id, @Valid @RequestBody Film film) {
        log.info("Обновлен фильм с id: {}", id);
        Film updatedFilm = films.stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ValidationException("Фильм не найден"));
        updatedFilm.setName(film.getName());
        updatedFilm.setDescription(film.getDescription());
        updatedFilm.setReleaseDate(film.getReleaseDate());
        updatedFilm.setDuration(film.getDuration());
        return updatedFilm;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return films;
    }
}
