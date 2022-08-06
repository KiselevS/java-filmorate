package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTest {
    private final FilmController filmController = new FilmController();

    @Test
    void emptyNameTest() {
        Film film = new Film(1, "", "description", LocalDate.of(2021, 11, 1), 100);
        assertFalse(filmController.validateFilm(film), "Не пройдена проверка на пустое имя");
    }

    @Test
    void longDescriptionTest() {
        StringBuilder sb = new StringBuilder();
        sb.append("t".repeat(201));
        Film film = new Film(1, "name", sb.toString(), LocalDate.of(2021, 11, 1), 100);
        assertFalse(filmController.validateFilm(film), "Не пройдена проверка на длинное описание");
    }

    @Test
    void releaseDateTest() {
        Film film1 = new Film(1, "name", "description", LocalDate.of(1895, 12, 29), 100);
        Film film2 = new Film(2, "name", "description", LocalDate.of(1895, 12, 28), 100);
        Film film3 = new Film(3, "name", "description", LocalDate.of(1895, 12, 27), 100);
        assertTrue(filmController.validateFilm(film1), "Не пройдена проверка даты релиза");
        assertTrue(filmController.validateFilm(film2), "Не пройдена проверка даты релиза");
        assertFalse(filmController.validateFilm(film3), "Не пройдена проверка даты релиза");
    }

    @Test
    void durationTest() {
        Film film = new Film(1, "name", "description", LocalDate.of(2021, 11, 1), -1);
        assertFalse(filmController.validateFilm(film), "Не пройдена проверка длительности");
    }
}