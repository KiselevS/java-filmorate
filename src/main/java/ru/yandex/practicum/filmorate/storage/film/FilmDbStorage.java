package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private static JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        FilmDbStorage.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        final String sqlQuery = "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, M.RATING_ID, M.NAME FROM FILMS AS F " +
                "JOIN MPA_RATINGS M ON F.MPA_RATING = M.RATING_ID WHERE FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, filmId);
        loadFilmGenre(films.get(0));
        return Optional.of(films.get(0));
    }

    @Override
    public List<Film> getFilms() {
        final String sqlQuery = "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, M.RATING_ID, M.NAME FROM FILMS AS F " +
                "JOIN MPA_RATINGS M ON F.MPA_RATING = M.RATING_ID";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        film.setId(simpleJdbcInsert.executeAndReturnKey(this.filmToMap(film)).longValue());
        setFilmGenre(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (getFilmById(film.getId()).isEmpty()){
            throw new NotFoundException();
        }
        String sqlQuery = "UPDATE FILMS SET NAME = ?, DESCRIPTION  = ?, RELEASE_DATE = ?, DURATION = ?, MPA_RATING = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        setFilmGenre(film);
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            loadFilmGenre(film);
        }
        return film;
    }


    private void setFilmGenre(Film film) {
        long filmId = film.getId();
        String sqlQuery = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }

        List<Long> filmGenres = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            filmGenres.add(genre.getId());
        }

        jdbcTemplate.batchUpdate("MERGE INTO FILM_GENRES (GENRE_ID, FILM_ID) VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, filmGenres.get(i));
                ps.setLong(2, filmId);
            }

            @Override
            public int getBatchSize() {
                return filmGenres.size();
            }
        });
    }


    private static void loadFilmGenre(Film film) {
        String sqlQuery = "SELECT GENRE_ID, NAME FROM GENRES " +
                "WHERE GENRE_ID IN (SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?) ORDER BY GENRE_ID";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, film.getId());
        Set<Genre> genres = new LinkedHashSet<>();
        while (rs.next()) {
            genres.add(new Genre(
                    rs.getLong("GENRE_ID"),
                    rs.getString("NAME")));
        }
        if (!genres.isEmpty()) {
            film.setGenres(genres);
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlQuery = "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, M.RATING_ID, M.NAME FROM FILMS F \n" +
                "LEFT JOIN LIKES L ON L.FILM_ID = F.FILM_ID JOIN MPA_RATINGS M ON F.MPA_RATING = M.RATING_ID \n" +
                "GROUP BY F.FILM_ID, L.USER_ID \n" +
                "ORDER BY COUNT(L.USER_ID) DESC\n" +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, count);
    }

    private static Film makeFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film(
                resultSet.getLong("FILM_ID"),
                resultSet.getString("NAME"),
                resultSet.getString("DESCRIPTION"),
                resultSet.getDate("RELEASE_DATE").toLocalDate(),
                resultSet.getDouble("DURATION"),
                new MPA(resultSet.getLong("MPA_RATINGS.RATING_ID"), resultSet.getString("MPA_RATINGS.NAME")));
        loadFilmGenre(film);
        return film;
    }

    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("NAME", film.getName());
        values.put("DESCRIPTION", film.getDescription());
        values.put("RELEASE_DATE", film.getReleaseDate());
        values.put("DURATION", film.getDuration());
        if (film.getMpa() != null) {
            values.put("MPA_RATING", film.getMpa().getId());
        }
        return values;
    }
}