package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceprion.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    public Film createFilm(Film film) {
        log.info("Создание фильма с name \"{}\"", film.getName());

        return inMemoryFilmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Изменение фильма с id \"{}\"", film.getId());

        return inMemoryFilmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        log.info("Отображение списка фильмов");

        return inMemoryFilmStorage.getAllFilms();
    }

    public void addLike(Long filmId, Long userId) {
        log.info("Добавление лайка фильму с id={} пользователем с id={}", filmId, userId);

        var film = inMemoryFilmStorage.getFilmById(filmId);

        if (film.getLikes().contains(userId)) {
            throw new AlreadyExistsException("Лайк уже поставлен на фильм с id=" + filmId);
        }

        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.info("Удаление лайка пользователем с id={} с фильма с id={}", userId, filmId);

        var film = inMemoryFilmStorage.getFilmById(filmId);

        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("Не найден лайк на фильме с id=" + filmId);
        }

        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        log.info("Отображение списка из {} популярных фильмов", count);

        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt((Film film) ->
                        film.getLikes() != null ? film.getLikes().size() : 0).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
