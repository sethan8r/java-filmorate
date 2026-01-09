package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreRequestDto;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<Genre> getAllGenres() {
        log.info("Получение списка всех жанров");
        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(Long id) {
        log.info("Получение жанра с id={}", id);
        return genreStorage.getGenreById(id);
    }

    public Genre createGenre(GenreRequestDto request) {
        log.info("Создание жанра с name=\"{}\"", request.name());

        Genre genre = new Genre();
        genre.setName(request.name());

        return genreStorage.createGenre(genre);
    }

    public Genre updateGenre(Long id, GenreRequestDto request) {
        log.info("Изменение жанра с id=\"{}\"", id);

        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(request.name());

        return genreStorage.updateGenre(genre);
    }

    public void deleteGenre(Long id) {
        log.info("Удаление жанра с id={}", id);
        if (!genreStorage.deleteGenre(id)) {
            throw new NotFoundException("Жанр с id=" + id + " не найден");
        }
    }

    public List<Genre> getFilmGenres(Long filmId) {
        log.info("Получение жанров фильма с id={}", filmId);
        return genreStorage.getFilmGenres(filmId);
    }

    public void addGenreToFilm(Long filmId, Long genreId) {
        log.info("Добавление жанра с id={} к фильму с id={}", genreId, filmId);
        genreStorage.addGenreToFilm(filmId, genreId);
    }

    public void removeGenreFromFilm(Long filmId, Long genreId) {
        log.info("Удаление жанра с id={} из фильма с id={}", genreId, filmId);
        genreStorage.removeGenreFromFilm(filmId, genreId);
    }
}
