package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       UserService userService,
                       MpaStorage mpaStorage,
                       GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public Film createFilm(Film film) {
        validateMpaAndGenres(film);
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        validateMpaAndGenres(film);
        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(int id) {
        return filmStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + id + " не найден"));
    }

    public void addLike(int filmId, int userId) {
        validateFilmAndUserExist(filmId, userId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        validateFilmAndUserExist(filmId, userId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopular(Math.max(count, 1)); // Гарантируем минимум 1 результат
    }

    private void validateMpaAndGenres(Film film) {
        Mpa mpa = mpaStorage.getMpaById(film.getMpa().getId());
        if (mpa == null) {
            throw new NotFoundException("MPA с ID " + film.getMpa().getId() + " не найден");
        }
        film.setMpa(mpa);

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                Genre existingGenre = genreStorage.getGenreById(genre.getId());
                if (existingGenre == null) {
                    throw new NotFoundException("Жанр с ID " + genre.getId() + " не найден");
                }
                genre.setName(existingGenre.getName());
            }
        }
    }

    private void validateFilmAndUserExist(int filmId, int userId) {
        getFilmById(filmId);
        userService.getUserById(userId);
    }
}