package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exceprion.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {

    @InjectMocks
    private FilmService service;

    @Test
    void validateFilmDescriptionTooLongTest() {
        Film film = new Film();
        film.setName("Имя фильма");
        film.setDescription("Мир на грани. Судьба — в руках изгоя. Легенда рождается в песках пустыни в" +
                " борьбе за будущее вселенной. Еще какие-то слова, что бы заработало это исключение. И еще слова, " +
                "что бы его выкинуть. И еще пару слов");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        assertThatThrownBy(() -> service.createFilm(film))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void validateFilmNameIsNullTest() {
        Film film = new Film();
        film.setDescription("Мир на грани. Судьба — в руках изгоя. Легенда рождается в песках пустыни в" +
                " борьбе за будущее вселенной");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        assertThatThrownBy(() -> service.createFilm(film))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void validateFilmReleaseDateIsBeforeTest() {
        Film film = new Film();
        film.setName("Имя фильма");
        film.setDescription("Мир на грани. Судьба — в руках изгоя. Легенда рождается в песках пустыни в" +
                " борьбе за будущее вселенной");
        film.setReleaseDate(LocalDate.of(1700, 1, 1));
        film.setDuration(120);

        assertThatThrownBy(() -> service.createFilm(film))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void validateFilmDurationIsZeroTest() {
        Film film = new Film();
        film.setName("Имя фильма");
        film.setDescription("Мир на грани. Судьба — в руках изгоя. Легенда рождается в песках пустыни в" +
                " борьбе за будущее вселенной");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-120);

        assertThatThrownBy(() -> service.createFilm(film))
                .isInstanceOf(ValidationException.class);
    }
}
