package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmRequestDto;
import ru.yandex.practicum.filmorate.model.AgeRating;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.agerating.AgeRatingStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final AgeRatingStorage ageRatingStorage;

    public Film createFilm(FilmRequestDto request) {
        log.info("Создание фильма с name \"{}\"", request.name());

        Film film = new Film();
        film.setName(request.name());
        film.setDescription(request.description());
        film.setReleaseDate(request.releaseDate());
        film.setDuration(request.duration());

        AgeRating ageRating = ageRatingStorage.getAgeRatingById(request.ageRatingId());
        film.setAgeRating(ageRating);

        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Long filmId, FilmRequestDto request) {
        log.info("Изменение фильма с id \"{}\"", filmId);

        Film film = new Film();
        film.setId(filmId);
        film.setName(request.name());
        film.setDescription(request.description());
        film.setReleaseDate(request.releaseDate());
        film.setDuration(request.duration());

        AgeRating ageRating = ageRatingStorage.getAgeRatingById(request.ageRatingId());
        film.setAgeRating(ageRating);

        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        log.info("Отображение списка фильмов");

        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long filmId) {
        log.info("Получение фильма с id={}", filmId);

        return filmStorage.getFilmById(filmId);
    }

    public void addLike(Long filmId, Long userId) {
        log.info("Добавление лайка фильму с id={} пользователем с id={}", filmId, userId);

        filmStorage.getFilmById(filmId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.info("Удаление лайка пользователем с id={} с фильма с id={}", userId, filmId);

        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        log.info("Отображение списка из {} популярных фильмов", count);

        return filmStorage.getPopularFilms(count);
    }
}
