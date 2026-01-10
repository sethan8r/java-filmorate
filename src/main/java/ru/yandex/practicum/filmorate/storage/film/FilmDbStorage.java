package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.Date;
import java.util.List;

@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {

    private static final String INSERT_QUERY =
            "INSERT INTO films(name, description, release_date, duration, age_rating_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY =
            "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, age_rating_id = ? " +
                    "WHERE id = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT f.*, ar.name as age_rating_name FROM films f " +
                    "LEFT JOIN age_ratings ar ON f.age_rating_id = ar.id";
    private static final String FIND_BY_ID_QUERY =
            "SELECT f.*, ar.name as age_rating_name FROM films f " +
                    "LEFT JOIN age_ratings ar ON f.age_rating_id = ar.id " +
                    "WHERE f.id = ?";
    private static final String ADD_LIKE_QUERY =
            "INSERT INTO likes(film_id, user_id) VALUES (?, ?)";
    private static final String REMOVE_LIKE_QUERY =
            "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String LIKE_EXISTS_QUERY =
            "SELECT EXISTS(SELECT 1 FROM likes WHERE film_id = ? AND user_id = ?)";
    private static final String GET_POPULAR_FILMS_QUERY =
            "SELECT f.*, ar.name as age_rating_name FROM films f " +
                    "LEFT JOIN age_ratings ar ON f.age_rating_id = ar.id " +
                    "LEFT JOIN likes l ON f.id = l.film_id GROUP BY f.id, f.name, f.description," +
                    " f.release_date, f.duration, f.age_rating_id, ar.id, ar.name " +
                    "ORDER BY COUNT(l.film_id) DESC " +
                    "LIMIT ?";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film createFilm(Film film) {
        Long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getAgeRating().getId()
        );
        film.setId(id);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getAgeRating().getId(),
                film.getId()
        );
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film getFilmById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        if (likeExists(filmId, userId)) {
            throw new IllegalArgumentException("Пользователь уже поставил лайк этому фильму");
        }
        jdbc.update(ADD_LIKE_QUERY, filmId, userId);
    }
    @Override
    public void removeLike(Long filmId, Long userId) {
        delete(REMOVE_LIKE_QUERY, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return findMany(GET_POPULAR_FILMS_QUERY, count);
    }

    private boolean likeExists(Long filmId, Long userId) {
        return jdbc.queryForObject(LIKE_EXISTS_QUERY, Boolean.class, filmId, userId);
    }
}
