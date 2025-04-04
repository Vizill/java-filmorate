package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDao;

import java.util.List;

@Repository
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM ratings";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Mpa(
                rs.getInt("id"),
                rs.getString("name")
        ));
    }

    @Override
    public Mpa getMpaById(int id) {
        String sql = "SELECT * FROM ratings WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Mpa(
                rs.getInt("id"),
                rs.getString("name")
        ), id);
    }
}