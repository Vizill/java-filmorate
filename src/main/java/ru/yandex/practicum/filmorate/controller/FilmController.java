package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

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
        validateFilm(film);
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
        validateFilm(film);

        for (Film f : films) {
            if (f.getId() == film.getId()) {
                f.setName(film.getName());
                f.setDescription(film.getDescription());
                f.setReleaseDate(film.getReleaseDate());
                f.setDuration(film.getDuration());
                return ResponseEntity.ok(f);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Фильм с ID " + film.getId() + " не найден.");
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не должно превышать 200 символов.");
        }
        if (film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}
