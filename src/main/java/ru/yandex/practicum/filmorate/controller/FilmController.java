package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody @Valid Film film) {

        return ResponseEntity.status(HttpStatus.CREATED).body(filmService.createFilm(film));
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {

        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {

        return filmService.getAllFilms();
    }
}
