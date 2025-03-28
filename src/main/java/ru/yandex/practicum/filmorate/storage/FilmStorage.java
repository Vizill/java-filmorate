package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film);
    Film update(Film film);
    List<Film> getAll();
    Optional<Film> getById(int id);
    void addLike(int filmId, int userId);
    void removeLike(int filmId, int userId);
    List<Film> getPopular(int count);
}