package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    User getUserById(Long id);

    boolean existsUserById(Long id);

    void addFriends(Long userId, Long friendId);

    void removeFriends(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long friendId);

    void confirmFriends(Long userId, Long friendId);

    void rejectFriends(Long userId, Long friendId);

    List<User> getFriendRequests(Long userId);
}
