package ru.yandex.practicum.filmorate.storage.agerating;

import ru.yandex.practicum.filmorate.model.AgeRating;

import java.util.List;

public interface AgeRatingStorage {
    List<AgeRating> getAllAgeRatings();

    AgeRating getAgeRatingById(Long id);

    AgeRating createAgeRating(AgeRating ageRating);

    AgeRating updateAgeRating(AgeRating ageRating);

    boolean deleteAgeRating(Long id);
}