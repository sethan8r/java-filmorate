package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(Long id);

    List<Film> getPopularFilms(Integer count);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);
}
