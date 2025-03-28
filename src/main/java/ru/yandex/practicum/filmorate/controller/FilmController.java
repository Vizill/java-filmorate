package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private int idCounter = 1;

    @PostMapping
    public ResponseEntity<?> createFilm(@RequestBody @Valid Film film) {
        film.setId(idCounter++);
        films.add(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getFilms() {
        return ResponseEntity.ok(films);
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@RequestBody @Valid Film film) {
        Optional<Film> existingFilm = films.stream()
                .filter(f -> f.getId() == film.getId())
                .findFirst();

        if (existingFilm.isEmpty()) {
            throw new NotFoundException("Фильм с ID " + film.getId() + " не найден.");
        }

        Film updatedFilm = existingFilm.get();
        updatedFilm.setName(film.getName());
        updatedFilm.setDescription(film.getDescription());
        updatedFilm.setReleaseDate(film.getReleaseDate());
        updatedFilm.setDuration(film.getDuration());

        return ResponseEntity.ok(updatedFilm);
    }
}