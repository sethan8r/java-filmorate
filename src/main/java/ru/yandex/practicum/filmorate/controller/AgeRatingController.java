package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.AgeRating;
import ru.yandex.practicum.filmorate.service.AgeRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class AgeRatingController {

    private final AgeRatingService ageRatingService;

    @GetMapping
    public List<AgeRating> getAllAgeRatings() {
        return ageRatingService.getAllAgeRatings();
    }

    @GetMapping("/{id}")
    public AgeRating getAgeRatingById(@PathVariable Long id) {
        return ageRatingService.getAgeRatingById(id);
    }

    @PostMapping
    public ResponseEntity<AgeRating> createAgeRating(@RequestBody AgeRating ageRating) {
        AgeRating created = ageRatingService.createAgeRating(ageRating);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public AgeRating updateAgeRating(@PathVariable Long id, @RequestBody AgeRating ageRating) {
        return ageRatingService.updateAgeRating(id, ageRating);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgeRating(@PathVariable Long id) {
        ageRatingService.deleteAgeRating(id);
        return ResponseEntity.noContent().build();
    }
}
