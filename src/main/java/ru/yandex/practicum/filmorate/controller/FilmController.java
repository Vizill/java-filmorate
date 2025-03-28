package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Film> createFilm(@RequestBody @Valid Film film) {
        film.setId(idCounter++);
        films.add(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getFilms() {
        return ResponseEntity.ok(films);
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@RequestBody @Valid Film updatedFilm) {
        Optional<Film> filmOptional = films.stream()
                .filter(f -> f.getId() == updatedFilm.getId())
                .findFirst();

        if (filmOptional.isEmpty()) {
            throw new NotFoundException("Фильм с ID " + updatedFilm.getId() + " не найден.");
        }

        Film existingFilm = filmOptional.get();
        existingFilm.setName(updatedFilm.getName());
        existingFilm.setDescription(updatedFilm.getDescription());
        existingFilm.setReleaseDate(updatedFilm.getReleaseDate());
        existingFilm.setDuration(updatedFilm.getDuration());

        return ResponseEntity.ok(existingFilm);
    }
}
