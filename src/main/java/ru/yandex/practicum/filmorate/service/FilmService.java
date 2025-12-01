package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceprion.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class FilmService {

    HashMap<Long, Film> films = new HashMap<>();

    private Long id = 1L;

    public Film createFilm(Film film) {
        log.info("Создание фильма с name \"{}\"", film.getName());

        validate(film);

        film.setId(nextId());
        films.put(film.getId(), film);

        return film;
    }

    public Film updateFilm(Film film) {
        log.info("Изменение фильма с id \"{}\"", film.getId());

        validate(film);

        films.put(film.getId(), film);

        return film;
    }

    public List<Film> getAllFilms() {
        log.info("Отображение списка фильмов");

        return new ArrayList<>(films.values());
    }

    private Long nextId() {
        return id++;
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}
