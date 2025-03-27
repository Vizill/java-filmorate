package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<Film> updateFilm(@RequestBody @Valid Film film) {
        if (film.getId() <= 0) {
            throw new ValidationException("ID фильма должен быть больше 0.");
        }

        for (Film f : films) {
            if (f.getId() == film.getId()) {
                f.setName(film.getName());
                f.setDescription(film.getDescription());
                f.setReleaseDate(film.getReleaseDate());
                f.setDuration(film.getDuration());
                return ResponseEntity.ok(f);
            }
        }

        throw new ValidationException("Фильм с ID " + film.getId() + " не найден.");
    }
}
