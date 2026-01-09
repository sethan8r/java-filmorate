package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.GenreRequestDto;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody @Valid GenreRequestDto request) {
        Genre created = genreService.createGenre(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public Genre updateGenre(@PathVariable Long id, @RequestBody @Valid GenreRequestDto request) {
        return genreService.updateGenre(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{filmId}/films")
    public List<Genre> getFilmGenres(@PathVariable Long filmId) {
        return genreService.getFilmGenres(filmId);
    }

    @PostMapping("/{filmId}/films/{genreId}")
    public ResponseEntity<Void> addGenreToFilm(@PathVariable Long filmId, @PathVariable Long genreId) {
        genreService.addGenreToFilm(filmId, genreId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{filmId}/films/{genreId}")
    public ResponseEntity<Void> removeGenreFromFilm(@PathVariable Long filmId, @PathVariable Long genreId) {
        genreService.removeGenreFromFilm(filmId, genreId);
        return ResponseEntity.noContent().build();
    }
}
