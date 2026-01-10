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
}