package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Получен запрос GET /films");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Получен запрос GET /films/{}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос GET /films/popular");
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Получен запрос POST /films");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос PUT /films");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос PUT /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос DELETE /films/{}/like/{}", id, userId);
        filmService.removeLike(id, userId);
    }
}
