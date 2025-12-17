package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @NotNull
    @PastOrPresent
    private LocalDate releaseDate;

    @NotNull
    @Positive
    private Integer duration;

    private Set<Long> likes = new HashSet<>();
}
