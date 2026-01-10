package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Repository
public class UserDbStorage extends BaseRepository<User> implements UserStorage {

    private static final String INSERT_QUERY =
            "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String EXISTS_QUERY = "SELECT EXISTS(SELECT 1 FROM users WHERE id = ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String ADD_FRIEND_QUERY =
            "INSERT INTO friendships(user_id, friend_id, is_confirmed) VALUES (?, ?, null)";
    private static final String CONFIRM_FRIEND_QUERY =
            "UPDATE friendships SET is_confirmed = true WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
    private static final String REJECT_FRIEND_QUERY =
            "UPDATE friendships SET is_confirmed = false WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
    private static final String REMOVE_FRIEND_QUERY =
            "DELETE FROM friendships WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
    private static final String GET_FRIENDS_QUERY =
            "SELECT u.* FROM users u " +
                    "JOIN friendships f ON u.id = f.friend_id " +
                    "WHERE f.user_id = ? AND f.is_confirmed = true " +
                    "UNION " +
                    "SELECT u.* FROM users u " +
                    "JOIN friendships f ON u.id = f.user_id " +
                    "WHERE f.friend_id = ? AND f.is_confirmed = true";
    private static final String GET_COMMON_FRIENDS_QUERY =
            "SELECT u.* FROM users u " +
                    "WHERE u.id IN (" +
                    "  SELECT f1.friend_id FROM friendships f1 WHERE f1.user_id = ? AND f1.is_confirmed = true " +
                    "  INTERSECT " +
                    "  SELECT f2.friend_id FROM friendships f2 WHERE f2.user_id = ? AND f2.is_confirmed = true" +
                    ")";

    private static final String GET_FRIEND_REQUESTS_QUERY =
            "SELECT u.* FROM users u " +
                    "JOIN friendships f ON u.id = f.user_id " +
                    "WHERE f.friend_id = ? AND f.is_confirmed IS NULL";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User createUser(User user) {
        Long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public List<User> getUsers() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User getUserById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }

    @Override
    public boolean existsUserById(Long id) {
        return jdbc.queryForObject(EXISTS_QUERY, Boolean.class, id);
    }

    @Override
    public void addFriends(Long userId, Long friendId) {
        insert(ADD_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public List<User> getFriendRequests(Long userId) {
        return findMany(GET_FRIEND_REQUESTS_QUERY, userId);
    }

    @Override
    public void confirmFriends(Long userId, Long friendId) {
        jdbc.update(CONFIRM_FRIEND_QUERY, userId, friendId, friendId, userId);
    }

    @Override
    public void rejectFriends(Long userId, Long friendId) {
        jdbc.update(REJECT_FRIEND_QUERY, userId, friendId, friendId, userId);
    }

    @Override
    public void removeFriends(Long userId, Long friendId) {
        jdbc.update(REMOVE_FRIEND_QUERY, userId, friendId, friendId, userId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return findMany(GET_FRIENDS_QUERY, userId, userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        return findMany(GET_COMMON_FRIENDS_QUERY, userId, friendId);
    }
}
