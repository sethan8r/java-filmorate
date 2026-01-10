package ru.yandex.practicum.filmorate.ittest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
class GenreDbStorageItTest {

    @Autowired
    private GenreDbStorage genreDbStorage;

    @Test
    public void testGetAllGenres() {
        List<Genre> genres = genreDbStorage.getAllGenres();

        assertThat(genres)
                .isNotEmpty()
                .hasSize(6)
                .extracting(Genre::getId)
                .containsExactly(1L, 2L, 3L, 4L, 5L, 6L);
    }

    @Test
    public void testGetGenreById() {
        Genre genre = genreDbStorage.getGenreById(1L);

        assertThat(genre)
                .isNotNull()
                .extracting(Genre::getId)
                .isEqualTo(1L);
        assertThat(genre.getName()).isNotNull();
    }

    @Test
    public void testGetGenreByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            genreDbStorage.getGenreById(999L);
        });
    }
}
