package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserRequestDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User createUser(UserRequestDto request) {
        log.info("Создание пользователя с login \"{}\"", request.login());

        User user = new User();
        user.setEmail(request.email());
        user.setLogin(request.login());
        user.setName(request.name() != null && !request.name().isEmpty() ? request.name() : request.login());
        user.setBirthday(request.birthday());

        return userStorage.createUser(user);
    }

    public User updateUser(Long userId, UserRequestDto request) {
        log.info("Изменение пользователя с id \"{}\"", userId);

        User user = new User();
        user.setId(userId);
        user.setEmail(request.email());
        user.setLogin(request.login());
        user.setName(request.name() != null && !request.name().isEmpty() ? request.name() : request.login());
        user.setBirthday(request.birthday());

        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        log.info("Получение всех пользователей");
        return userStorage.getUsers();
    }

    public User getUserById(Long userId) {
        log.info("Получение пользователя с id={}", userId);
        return userStorage.getUserById(userId);
    }

    public void addFriends(Long userId, Long friendId) {
        log.info("Отправка заявки в друзья от пользователя с id={} к id={}", userId, friendId);
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);
        userStorage.addFriends(userId, friendId);
    }

    public void confirmFriends(Long userId, Long friendId) {
        log.info("Принятие заявки в друзья от пользователя с id={} для id={}", friendId, userId);
        userStorage.confirmFriends(userId, friendId);
    }

    public void rejectFriends(Long userId, Long friendId) {
        log.info("Отклонение заявки в друзья от пользователя с id={} для id {}", friendId, userId);
        userStorage.rejectFriends(userId, friendId);
    }

    public void removeFriends(Long userId, Long friendId) {
        log.info("Удаление из друзей пользователя с id={} пользователем с id={}", friendId, userId);
        userStorage.removeFriends(userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        log.info("Получение друзей пользователя с id={}", userId);
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        log.info("Получение общих друзей пользователя с id={} и id={}", userId, friendId);
        return userStorage.getCommonFriends(userId, friendId);
    }

    public List<User> getFriendRequests(Long userId) {
        log.info("Получение входящих заявок в друзья для пользователя с id={}", userId);
        return userStorage.getFriendRequests(userId);
    }
}
