package ru.yandex.practicum.filmorate.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private int idCounter = 1;
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

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
        Film existingFilm = films.stream()
                .filter(f -> f.getId() == film.getId())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + film.getId() + " не найден."));

        existingFilm.setName(film.getName());
        existingFilm.setDescription(film.getDescription());
        existingFilm.setReleaseDate(film.getReleaseDate());
        existingFilm.setDuration(film.getDuration());

        return ResponseEntity.ok(existingFilm);
    }

}
