package ru.yandex.practicum.filmorate.ittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.AgeRating;
import ru.yandex.practicum.filmorate.storage.agerating.AgeRatingDbStorage;
import ru.yandex.practicum.filmorate.storage.agerating.AgeRatingMapper;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
class AgeRatingDbStorageItTest {

    @Autowired
    private AgeRatingDbStorage ageRatingDbStorage;

    private AgeRating testRating;

    @BeforeEach
    void setUp() {
        AgeRating newRating = new AgeRating();
        newRating.setName("TestRating" + System.currentTimeMillis());
        testRating = ageRatingDbStorage.createAgeRating(newRating);
    }

    @Test
    public void testGetAllAgeRatings() {
        List<AgeRating> ageRatings = ageRatingDbStorage.getAllAgeRatings();
        assertThat(ageRatings).isNotEmpty();
    }

    @Test
    public void testCreateAgeRating() {
        AgeRating newRating = new AgeRating();
        newRating.setName("NewRating" + System.currentTimeMillis());

        AgeRating created = ageRatingDbStorage.createAgeRating(newRating);

        assertThat(created)
                .isNotNull()
                .extracting(AgeRating::getName)
                .isNotNull();
    }

    @Test
    public void testUpdateAgeRating() {
        testRating.setName("Updated" + System.currentTimeMillis());

        AgeRating updated = ageRatingDbStorage.updateAgeRating(testRating);

        assertThat(updated.getName()).startsWith("Updated");
    }

    @Test
    public void testDeleteAgeRating() {
        boolean deleted = ageRatingDbStorage.deleteAgeRating(testRating.getId());
        assertThat(deleted).isTrue();
    }

    @Test
    public void testGetAgeRatingByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            ageRatingDbStorage.getAgeRatingById(999L);
        });
    }
}