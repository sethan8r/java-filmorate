package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();

    private Long id = 1L;

    public Film createFilm(Film film) {
        film.setId(nextId());
        films.put(film.getId(), film);

        return film;
    }

    public Film updateFilm(Film film) {
        films.put(film.getId(), film);

        return film;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private Long nextId() {
        return id++;
    }

    @Override
    public Film getFilmById(Long id) {
        return Optional.ofNullable(films.get(id))
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
    }
}
