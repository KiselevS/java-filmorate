package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component("LikeDbStorage")
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sqlQuery = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setLong(1, filmId);
            stmt.setLong(2, userId);
            return stmt;
        });
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }
}