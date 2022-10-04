package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage,
                       @Qualifier("LikeDbStorage") LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public Optional<Film> getFilmById(long filmId) {
        if (filmId < 1) {
            throw new NotFoundException();
        }
        if (filmStorage.getFilmById(filmId).isEmpty()){
            throw new NotFoundException();
        }
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException();
        }
        if (film.getMpa() == null) {
            throw new ValidationException();
        }
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if (film.getId() < 1) {
            throw new NotFoundException();
        }
        if (getFilmById(film.getId()).isEmpty()){
            throw new NotFoundException();
        }
        return filmStorage.updateFilm(film);
    }

    public void addLike(long filmId, long userId) {
        if (filmId < 1) {
            throw new NotFoundException();
        }
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isPresent()) {
            likeStorage.addLike(filmId, userId);
        } else {
            throw new NotFoundException();
        }
    }

    public void removeLike(long filmId, long userId) {
        if (filmId < 1) {
            throw new NotFoundException();
        }
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isPresent()) {
            likeStorage.deleteLike(filmId, userId);
        } else {
            throw new NotFoundException();
        }
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
