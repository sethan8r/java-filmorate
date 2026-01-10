package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Repository
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM genres ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";

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
}
