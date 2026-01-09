package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.AgeRating;
import ru.yandex.practicum.filmorate.storage.agerating.AgeRatingStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AgeRatingService {

    private final AgeRatingStorage ageRatingStorage;

    public List<AgeRating> getAllAgeRatings() {
        log.info("Получение списка всех рейтингов");
        return ageRatingStorage.getAllAgeRatings();
    }

    public AgeRating getAgeRatingById(Long id) {
        log.info("Получение рейтинга с id={}", id);
        return ageRatingStorage.getAgeRatingById(id);
    }

    public AgeRating createAgeRating(AgeRating ageRating) {
        log.info("Создание рейтинга с name=\"{}\"", ageRating.getName());
        return ageRatingStorage.createAgeRating(ageRating);
    }

    public AgeRating updateAgeRating(Long id, AgeRating ageRating) {
        log.info("Изменение рейтинга с id=\"{}\"", id);
        ageRating.setId(id);
        return ageRatingStorage.updateAgeRating(ageRating);
    }

    public void deleteAgeRating(Long id) {
        log.info("Удаление рейтинга с id=\"{}\"", id);
        if (!ageRatingStorage.deleteAgeRating(id)) {
            throw new NotFoundException("Рейтинг с id=" + id + " не найден");
        }
    }
}
