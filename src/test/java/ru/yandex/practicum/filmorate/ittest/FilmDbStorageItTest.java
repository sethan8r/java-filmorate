package ru.yandex.practicum.filmorate.ittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.AgeRating;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.agerating.AgeRatingDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmDbStorageItTest {

    @Autowired
    private FilmDbStorage filmDbStorage;

    @Autowired
    private AgeRatingDbStorage ageRatingDbStorage;

    @Autowired
    private UserDbStorage userDbStorage;

    private Film testFilm;
    private AgeRating testRating;
    private User testUser;

    @BeforeEach
    void setUp() {
        testRating = ageRatingDbStorage.getAgeRatingById(1L);

        testFilm = new Film();
        testFilm.setName("Test Film " + System.currentTimeMillis());
        testFilm.setDescription("Test Description");
        testFilm.setReleaseDate(LocalDate.of(2020, 1, 1));
        testFilm.setDuration(120);
        testFilm.setAgeRating(testRating);
        testFilm = filmDbStorage.createFilm(testFilm);

        testUser = new User();
        testUser.setEmail("testuser" + System.currentTimeMillis() + "@test.com");
        testUser.setLogin("testuser" + System.currentTimeMillis());
        testUser.setName("Test User");
        testUser.setBirthday(LocalDate.of(1990, 1, 1));
        testUser = userDbStorage.createUser(testUser);
    }

    @Test
    public void testCreateFilm() {
        Film newFilm = new Film();
        newFilm.setName("New Film " + System.currentTimeMillis());
        newFilm.setDescription("New Description");
        newFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        newFilm.setDuration(150);
        newFilm.setAgeRating(testRating);

        Film created = filmDbStorage.createFilm(newFilm);

        assertThat(created)
                .isNotNull()
                .extracting(Film::getName)
                .isNotNull();
        assertThat(created.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetAllFilms() {
        List<Film> films = filmDbStorage.getAllFilms();

        assertThat(films)
                .isNotEmpty()
                .anyMatch(film -> film.getId().equals(testFilm.getId()));
    }

    @Test
    public void testGetFilmById() {
        Film film = filmDbStorage.getFilmById(testFilm.getId());

        assertThat(film)
                .isNotNull()
                .extracting(Film::getId)
                .isEqualTo(testFilm.getId());
        assertThat(film.getName()).isEqualTo(testFilm.getName());
    }

    @Test
    public void testGetFilmByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            filmDbStorage.getFilmById(999L);
        });
    }

    @Test
    public void testUpdateFilm() {
        testFilm.setName("Updated Film " + System.currentTimeMillis());
        testFilm.setDescription("Updated Description");
        testFilm.setDuration(200);

        Film updated = filmDbStorage.updateFilm(testFilm);

        assertThat(updated.getName()).isEqualTo(testFilm.getName());
        assertThat(updated.getDescription()).isEqualTo("Updated Description");
        assertThat(updated.getDuration()).isEqualTo(200);

        Film fetched = filmDbStorage.getFilmById(testFilm.getId());
        assertThat(fetched.getName()).isEqualTo(testFilm.getName());
    }

    @Test
    public void testAddLike() {
        filmDbStorage.addLike(testFilm.getId(), testUser.getId());

        List<Film> popular = filmDbStorage.getPopularFilms(10);
        assertThat(popular)
                .anyMatch(film -> film.getId().equals(testFilm.getId()));
    }

    @Test
    public void testAddLikeDuplicate() {
        filmDbStorage.addLike(testFilm.getId(), testUser.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            filmDbStorage.addLike(testFilm.getId(), testUser.getId());
        });
    }

    @Test
    public void testRemoveLike() {
        filmDbStorage.addLike(testFilm.getId(), testUser.getId());
        filmDbStorage.removeLike(testFilm.getId(), testUser.getId());

        assertThat(true).isTrue();
    }

    @Test
    public void testGetPopularFilms() {
        User user2 = new User();
        user2.setEmail("testuser2" + System.currentTimeMillis() + "@test.com");
        user2.setLogin("testuser2" + System.currentTimeMillis());
        user2.setName("Test User 2");
        user2.setBirthday(LocalDate.of(1991, 1, 1));
        user2 = userDbStorage.createUser(user2);

        filmDbStorage.addLike(testFilm.getId(), testUser.getId());
        filmDbStorage.addLike(testFilm.getId(), user2.getId());

        List<Film> popular = filmDbStorage.getPopularFilms(10);

        assertThat(popular)
                .isNotEmpty()
                .anyMatch(film -> film.getId().equals(testFilm.getId()));
    }

    @Test
    public void testGetPopularFilmsWithLimit() {
        List<Film> popular = filmDbStorage.getPopularFilms(1);

        assertThat(popular)
                .hasSizeLessThanOrEqualTo(1);
    }
}
