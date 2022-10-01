package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(value = "/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос GET /films");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Optional<Film> getFilmById(@PathVariable long id) {
        log.info("Получен запрос GET /films/{}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) int count) {
        log.info("Получен запрос GET /films/popular");
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST /films");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT /films");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос PUT /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос DELETE /films/{}/like/{}", id, userId);
        filmService.removeLike(id, userId);
    }
}
