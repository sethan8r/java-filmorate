package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Repository
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM genres ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO genres(name) VALUES (?) RETURNING id";
    private static final String UPDATE_QUERY = "UPDATE genres SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM genres WHERE id = ?";
    private static final String CHECK_GENRE_IN_USE_QUERY =
            "SELECT EXISTS(SELECT 1 FROM film_genres WHERE genre_id = ?)";
    private static final String GET_FILM_GENRES_QUERY = "SELECT g.* FROM genres g " +
            "JOIN film_genres fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
    private static final String ADD_GENRE_TO_FILM_QUERY = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";
    private static final String REMOVE_GENRE_FROM_FILM_QUERY =
            "DELETE FROM film_genres WHERE film_id = ? AND genre_id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Genre> getAllGenres() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Genre getGenreById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException("Жанр с id=" + id + " не найден"));
    }

    @Override
    public Genre createGenre(Genre genre) {
        Long id = insert(INSERT_QUERY, genre.getName());
        genre.setId(id);
        return genre;
    }

    @Override
    public Genre updateGenre(Genre genre) {
        update(UPDATE_QUERY, genre.getName(), genre.getId());
        return genre;
    }

    @Override
    public boolean deleteGenre(Long id) {
        if (genreInUse(id)) {
            throw new IllegalArgumentException("Невозможно удалить жанр, он используется в фильмах");
        }
        return delete(DELETE_QUERY, id);
    }

    @Override
    public List<Genre> getFilmGenres(Long filmId) {
        return findMany(GET_FILM_GENRES_QUERY, filmId);
    }

    @Override
    public void addGenreToFilm(Long filmId, Long genreId) {
        insert(ADD_GENRE_TO_FILM_QUERY, filmId, genreId);
    }

    @Override
    public void removeGenreFromFilm(Long filmId, Long genreId) {
        jdbc.update(REMOVE_GENRE_FROM_FILM_QUERY, filmId, genreId);
    }

    private boolean genreInUse(Long genreId) {
        return jdbc.queryForObject(CHECK_GENRE_IN_USE_QUERY, Boolean.class, genreId);
    }
}
