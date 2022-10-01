package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreDbStorage genreStorage;

    @Autowired
    public GenreService(GenreDbStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Optional<Genre> getGenreById(long genreId) {
        Optional<Genre> genre = genreStorage.getGenreById(genreId);
        if (genre.isEmpty()) {
            throw new NotFoundException();
        }
        return genre;
    }

    public List<Genre> getGenres() {
        return genreStorage.getAllGenres();
    }
}
