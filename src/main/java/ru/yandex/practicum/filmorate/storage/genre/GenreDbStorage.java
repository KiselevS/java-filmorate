package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> getGenreById(long id) {
        String sqlQuery = "SELECT GENRE_ID, NAME FROM GENRES WHERE GENRE_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(
                sqlQuery, id);
        if (rs.next()) {
            return Optional.of(new Genre(
                    rs.getLong("GENRE_ID"),
                    rs.getString("NAME")));
        }
        return Optional.empty();
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT GENRE_ID, NAME FROM GENRES";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Genre(
                resultSet.getLong("GENRE_ID"),
                resultSet.getString("NAME"));
    }
}
