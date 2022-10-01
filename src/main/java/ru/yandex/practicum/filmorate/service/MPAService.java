package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MPADbStorage;

import java.util.List;
import java.util.Optional;

@Service
public class MPAService {
    private final MPADbStorage mpaStorage;

    @Autowired
    public MPAService(MPADbStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }


    public Optional<MPA> getRatingById(long ratingId) {
        Optional<MPA> mpaRating = mpaStorage.getRatingById(ratingId);
        if (mpaRating.isEmpty() || ratingId < 1) {
            throw new NotFoundException();
        }
        return mpaRating;
    }


    public List<MPA> getRatings() {
        return mpaStorage.getAllRatings();
    }
}
