package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa_ratings";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {
        String sql = "SELECT * FROM mpa_ratings WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}