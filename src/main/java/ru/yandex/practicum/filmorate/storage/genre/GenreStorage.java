package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAllGenres();

    Genre getGenreById(Long id);

    Genre createGenre(Genre genre);

    Genre updateGenre(Genre genre);

    boolean deleteGenre(Long id);

    List<Genre> getFilmGenres(Long filmId);

    void addGenreToFilm(Long filmId, Long genreId);

    void removeGenreFromFilm(Long filmId, Long genreId);
}
