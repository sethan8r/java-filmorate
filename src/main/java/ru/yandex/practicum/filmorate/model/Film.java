package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private Long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private Integer duration;

    private AgeRating ageRating;

//    private Set<Long> likes = new HashSet<>();
}
