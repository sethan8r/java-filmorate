package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;

public record GenreRequestDto(
        @NotBlank
        String name
) {
}
