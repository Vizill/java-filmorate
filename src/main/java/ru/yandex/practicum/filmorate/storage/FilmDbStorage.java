package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        updateFilmGenres(film);
        film.setLikes(new HashSet<>());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE id = ?";

        int rowsUpdated = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        if (rowsUpdated == 0) {
            throw new NotFoundException("Фильм с ID " + film.getId() + " не найден");
        }

        updateFilmGenres(film);

        loadAdditionalData(film);

        return film;
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, this::mapRowToFilm).stream()
                .peek(this::loadAdditionalData)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Film> getById(int id) {
        String sql = "SELECT * FROM films WHERE id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
            loadAdditionalData(film);
            return Optional.of(film);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Film> getPopular(int count) {
        String sql = "SELECT f.*, COUNT(l.user_id) AS likes_count " +
                "FROM films f LEFT JOIN likes l ON f.id = l.film_id " +
                "GROUP BY f.id ORDER BY likes_count DESC LIMIT ?";
        return jdbcTemplate.query(sql, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpaStorage.getMpaById(rs.getInt("mpa_id")))
                .genres(new HashSet<>())
                .likes(new HashSet<>())
                .build();
    }

    private void loadAdditionalData(Film film) {
        loadGenres(film);
        loadLikes(film);
    }

    private void loadGenres(Film film) {
        String sql = "SELECT g.id, g.name FROM film_genres fg " +
                "JOIN genres g ON fg.genre_id = g.id " +
                "WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, this::mapRowToGenre, film.getId());
        film.setGenres(new HashSet<>(genres));
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }


    private void loadLikes(Film film) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        List<Integer> likes = jdbcTemplate.queryForList(sql, Integer.class, film.getId());
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        film.getLikes().addAll(likes);
    }

    private void updateFilmGenres(Film film) {
        String deleteSql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteSql, film.getId());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String insertSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            List<Object[]> batchArgs = film.getGenres()
                    .stream()
                    .map(genre -> new Object[]{film.getId(), genre.getId()})
                    .collect(Collectors.toList());
            jdbcTemplate.batchUpdate(insertSql, batchArgs);
        }
    }
}