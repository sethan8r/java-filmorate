package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {

    HashMap<Long, User> users = new HashMap<>();

    private Long id = 1L;

    @Override
    public User createUser(User user) {
        user.setId(nextId());
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private Long nextId() {
        return id++;
    }

    @Override
    public User getUserById(Long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }

    @Override
    public boolean existsUserById(Long id) {
        return users.containsKey(id);
    }
}
