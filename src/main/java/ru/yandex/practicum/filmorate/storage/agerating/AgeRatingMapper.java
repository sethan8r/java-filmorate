package ru.yandex.practicum.filmorate.storage.agerating;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.AgeRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AgeRatingMapper implements RowMapper<AgeRating> {
    @Override
    public AgeRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        var ageRating = new AgeRating();
        ageRating.setId(rs.getLong("id"));
        ageRating.setName(rs.getString("name"));
        return ageRating;
    }
}
