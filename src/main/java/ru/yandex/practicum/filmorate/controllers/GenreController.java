package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(value = "/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getGenres() {
        log.info("Получен запрос GET /genres");
        return genreService.getGenres();
    }

    @GetMapping("/{id}")
    public Optional<Genre> getGenreById(@PathVariable long id) {
        log.info("Получен запрос GET /genres/{}", id);
        return genreService.getGenreById(id);
    }
}