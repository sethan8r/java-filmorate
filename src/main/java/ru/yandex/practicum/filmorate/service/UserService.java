package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceprion.AlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    public User createUser(User user) {
        log.info("Создание пользователя с login \"{}\"", user.getLogin());

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        return inMemoryUserStorage.createUser(user);
    }

    public User updateUser(User user) {
        log.info("Изменение пользователя с id \"{}\"", user.getId());

        return inMemoryUserStorage.updateUser(user);
    }

    public List<User> getUsers() {
        log.info("Отображение всех пользователей");

        return inMemoryUserStorage.getUsers();
    }

    public void addFriends(Long userId, Long friendId) {
        log.info("Добавление пользователем с id={} в друзья пользователя с id={}", userId, friendId);

        var user = inMemoryUserStorage.getUserById(userId);
        var friend = inMemoryUserStorage.getUserById(friendId);

        if (user.getFriends().contains(friendId) || friend.getFriends().contains(userId)) {
            throw new AlreadyExistsException("Пользователи c id=" + friendId + " и id=" + friendId + " уже в друзьях");
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriends(Long userId, Long friendId) {
        log.info("Удаление пользователем с id={} из друзей пользователя с id={}", userId, friendId);

        var user = inMemoryUserStorage.getUserById(userId);
        var friend = inMemoryUserStorage.getUserById(friendId);

        if (!user.getFriends().contains(friendId) || !friend.getFriends().contains(userId)) {
            throw new AlreadyExistsException("Пользователи c id=" + friendId + " и id=" + friendId + " не в друзьях");
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(Long userId) {
        log.info("Отображение списка друзей пользователя с id={}", userId);

        return inMemoryUserStorage.getUserById(userId).getFriends().stream()
                .map(inMemoryUserStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        log.info("Получение общих друзей пользователей с id={} и id={}", userId, friendId);

        var user = inMemoryUserStorage.getUserById(userId);
        var friend = inMemoryUserStorage.getUserById(friendId);

        Set<Long> commonFriendIds = new HashSet<>(user.getFriends());
        commonFriendIds.retainAll(friend.getFriends());

        return commonFriendIds.stream()
                .map(inMemoryUserStorage::getUserById)
                .collect(Collectors.toList());
    }
}
