package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserRequestDto(
        @Email
        String email,
        @NotBlank
        String login,
        String name,
        @NotNull
        LocalDate birthday
) {
}
