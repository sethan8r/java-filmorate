package ru.yandex.practicum.filmorate.storage.agerating;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.AgeRating;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Repository
public class AgeRatingDbStorage extends BaseRepository<AgeRating> implements AgeRatingStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM age_ratings ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM age_ratings WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO age_ratings(name) VALUES (?)";
    private static final String UPDATE_QUERY = "UPDATE age_ratings SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM age_ratings WHERE id = ?";

    public AgeRatingDbStorage(JdbcTemplate jdbc, RowMapper<AgeRating> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<AgeRating> getAllAgeRatings() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public AgeRating getAgeRatingById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException("Рейтинг с id=" + id + " не найден"));
    }

    @Override
    public AgeRating createAgeRating(AgeRating ageRating) {
        Long id = insert(INSERT_QUERY, ageRating.getName());
        ageRating.setId(id);
        return ageRating;
    }

    @Override
    public AgeRating updateAgeRating(AgeRating ageRating) {
        update(UPDATE_QUERY, ageRating.getName(), ageRating.getId());
        return ageRating;
    }

    @Override
    public boolean deleteAgeRating(Long id) {
        return delete(DELETE_QUERY, id);
    }
}
