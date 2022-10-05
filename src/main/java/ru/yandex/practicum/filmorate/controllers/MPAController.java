package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(value = "/mpa")
public class MPAController {

    private final MPAService mpaService;

    @Autowired
    public MPAController(MPAService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<MPA> getRatings() {
        log.info("Получен запрос GET /mpa");
        return mpaService.getRatings();
    }

    @GetMapping("/{id}")
    public Optional<MPA> getRatingById(@PathVariable long id) {
        log.info("Получен запрос GET /mpa/{}", id);
        return mpaService.getRatingById(id);
    }
}