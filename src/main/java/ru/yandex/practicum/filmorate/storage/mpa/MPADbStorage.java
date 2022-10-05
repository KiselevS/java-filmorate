package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MPADbStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<MPA> getRatingById(long id) {
        String sqlQuery = "SELECT RATING_ID, NAME FROM MPA_RATINGS WHERE RATING_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (rs.next()) {
            return Optional.of(new MPA(
                    rs.getLong("RATING_ID"),
                    rs.getString("NAME")));
        }
        return Optional.empty();
    }

    @Override
    public List<MPA> getAllRatings() {
        String sqlQuery = "SELECT RATING_ID, NAME FROM MPA_RATINGS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToRating);
    }

    private MPA mapRowToRating(ResultSet resultSet, int rowNumber) throws SQLException {
        return new MPA(
                resultSet.getLong("RATING_ID"),
                resultSet.getString("NAME"));
    }
}
