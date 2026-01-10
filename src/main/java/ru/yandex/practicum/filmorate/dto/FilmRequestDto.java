package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record FilmRequestDto(
        @NotBlank
        String name,

        @Size(max = 200)
        String description,

        @NotNull
        @PastOrPresent
        LocalDate releaseDate,

        @NotNull
        @Positive
        Integer duration,

        @NotNull
        Long ageRatingId
) {
}
