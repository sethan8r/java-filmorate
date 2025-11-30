package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceprion.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class UserService {

    HashMap<Long, User> users =  new HashMap<>();

    private Long id = 1L;

    public User createUser(User user) {
        log.info("Создание пользователя с login \"{}\"", user.getLogin());

        validate(user);

        user.setId(nextId());

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);

        return user;
    }

    public User updateUser(User user) {
        log.info("Изменение пользователя с id \"{}\"", user.getId());

        validate(user);

        users.put(user.getId(), user);

        return user;
    }

    public List<User> getUsers() {
        log.info("Отображение всех пользователей");

        return new ArrayList<>(users.values());
    }

    private Long nextId() {
        return id++;
    }

    private void validate(User user) {
        if(user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        if(user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if(user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
