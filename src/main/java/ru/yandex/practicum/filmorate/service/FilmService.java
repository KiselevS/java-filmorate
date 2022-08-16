package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LocalDate RELEASE_MINIMUM = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        if (validateFilm(film)) {
            log.debug("Фильм с id = {} добавлен", film.getId());
            return filmStorage.addFilm(film);
        } else {
            log.error("Валидация не пройдена");
            throw new ValidationException();
        }
    }

    public Film updateFilm(Film film) {
        if (filmStorage.getFilmById(film.getId()) != null) {
            if (validateFilm(film)) {
                log.debug("Фильм с id = {} обновлен", film.getId());
                return filmStorage.updateFilm(film);
            } else {
                log.error("Валидация не пройдена");
                throw new ValidationException();
            }
        } else {
            throw new NotFoundException();
        }
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    private boolean validateFilm(Film film) {
        if (!film.getName().isBlank()
                && film.getDescription().length() <= 200
                && film.getReleaseDate().isAfter(RELEASE_MINIMUM.minusDays(1))
                && film.getDuration() >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public Film getFilmById(Long id) {
        final Film film = filmStorage.getFilmById(id);
        if (film != null) {
            return film;
        } else {
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
    }

    public void addLike(Long id, Long userId) {
        final Film film = filmStorage.getFilmById(id);
        final User user = userStorage.getUserById(userId);
        if (film != null && user != null) {
            film.getUsersLikes().add(userId);
        } else {
            throw new NotFoundException();
        }
    }

    public void removeLike(Long id, Long userId) {
        final Film film = filmStorage.getFilmById(id);
        final User user = userStorage.getUserById(userId);
        if (film != null && user != null) {
            film.getUsersLikes().remove(userId);
        } else {
            throw new NotFoundException();
        }
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

}
