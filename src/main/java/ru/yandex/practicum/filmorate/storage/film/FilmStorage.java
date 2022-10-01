package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    List<Film> getPopularFilms(int count);

    Optional<Film> getFilmById(long filmId);

    void setFilmGenre(Film film);

    void loadFilmGenre(Film film);
}
