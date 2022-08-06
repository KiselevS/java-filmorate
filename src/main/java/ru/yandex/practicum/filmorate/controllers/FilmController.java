package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private HashMap<Integer, Film> films = new HashMap<>();
    private static int id;
    private final LocalDate RELEASE_MINIMUM = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        log.debug("Получен запрос POST /films");
        if (validateFilm(film)) {
            film.setId(++id);
            films.put(id, film);
            log.debug("Фильм с id = {} добавлен", id);
            return film;
        } else {
            log.error("Валидация не пройдена");
            throw new ValidationException();
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        log.debug("Получен запрос PUT /films");
        if (validateFilm(film) && films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Фильм с id = {} обновлен", film.getId());
            return film;
        } else {
            log.error("Валидация не пройдена");
            throw new ValidationException();
        }

    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.debug("Получен запрос GET /films");
        return films.values();
    }

    protected boolean validateFilm(Film film) {
        if (!film.getName().isBlank()
                && film.getDescription().length() <= 200
                && film.getReleaseDate().isAfter(RELEASE_MINIMUM.minusDays(1))
                && film.getDuration() >= 0) {
            return true;
        } else {
            return false;
        }
    }
}
