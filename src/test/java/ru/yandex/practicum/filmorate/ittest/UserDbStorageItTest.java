package ru.yandex.practicum.filmorate.ittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
class UserDbStorageItTest {

    @Autowired
    private UserDbStorage userDbStorage;

    private User testUser;
    private User friendUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("testuser" + System.currentTimeMillis() + "@test.com");
        testUser.setLogin("testuser" + System.currentTimeMillis());
        testUser.setName("Test User");
        testUser.setBirthday(LocalDate.of(1990, 1, 1));
        testUser = userDbStorage.createUser(testUser);

        friendUser = new User();
        friendUser.setEmail("friend" + System.currentTimeMillis() + "@test.com");
        friendUser.setLogin("friend" + System.currentTimeMillis());
        friendUser.setName("Friend User");
        friendUser.setBirthday(LocalDate.of(1992, 6, 15));
        friendUser = userDbStorage.createUser(friendUser);
    }

    @Test
    public void testCreateUser() {
        User newUser = new User();
        newUser.setEmail("new" + System.currentTimeMillis() + "@test.com");
        newUser.setLogin("new" + System.currentTimeMillis());
        newUser.setName("New User");
        newUser.setBirthday(LocalDate.of(1995, 3, 20));

        User created = userDbStorage.createUser(newUser);

        assertThat(created)
                .isNotNull()
                .extracting(User::getName)
                .isEqualTo("New User");
        assertThat(created.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetUsers() {
        List<User> users = userDbStorage.getUsers();

        assertThat(users)
                .isNotEmpty()
                .anyMatch(user -> user.getId().equals(testUser.getId()));
    }

    @Test
    public void testGetUserById() {
        User user = userDbStorage.getUserById(testUser.getId());

        assertThat(user)
                .isNotNull()
                .extracting(User::getId)
                .isEqualTo(testUser.getId());
        assertThat(user.getLogin()).isEqualTo(testUser.getLogin());
    }

    @Test
    public void testGetUserByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            userDbStorage.getUserById(999L);
        });
    }

    @Test
    public void testUpdateUser() {
        testUser.setName("Updated Name " + System.currentTimeMillis());
        testUser.setEmail("updated" + System.currentTimeMillis() + "@test.com");

        User updated = userDbStorage.updateUser(testUser);

        assertThat(updated.getName()).isEqualTo(testUser.getName());
        assertThat(updated.getEmail()).isEqualTo(testUser.getEmail());

        User fetched = userDbStorage.getUserById(testUser.getId());
        assertThat(fetched.getName()).isEqualTo(testUser.getName());
    }

    @Test
    public void testExistsUserById() {
        boolean exists = userDbStorage.existsUserById(testUser.getId());

        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsUserByIdNotFound() {
        boolean exists = userDbStorage.existsUserById(999L);

        assertThat(exists).isFalse();
    }

    @Test
    public void testAddFriends() {
        userDbStorage.addFriends(testUser.getId(), friendUser.getId());

        List<User> requests = userDbStorage.getFriendRequests(friendUser.getId());
        assertThat(requests)
                .anyMatch(user -> user.getId().equals(testUser.getId()));
    }

    @Test
    public void testConfirmFriends() {
        userDbStorage.addFriends(testUser.getId(), friendUser.getId());
        userDbStorage.confirmFriends(friendUser.getId(), testUser.getId());

        List<User> friends = userDbStorage.getFriends(testUser.getId());
        assertThat(friends)
                .anyMatch(user -> user.getId().equals(friendUser.getId()));
    }

    @Test
    public void testRejectFriends() {
        userDbStorage.addFriends(testUser.getId(), friendUser.getId());
        userDbStorage.rejectFriends(friendUser.getId(), testUser.getId());

        List<User> requests = userDbStorage.getFriendRequests(friendUser.getId());
        assertThat(requests)
                .noneMatch(user -> user.getId().equals(testUser.getId()));
    }

    @Test
    public void testRemoveFriends() {
        userDbStorage.addFriends(testUser.getId(), friendUser.getId());
        userDbStorage.confirmFriends(friendUser.getId(), testUser.getId());
        userDbStorage.removeFriends(testUser.getId(), friendUser.getId());

        List<User> friends = userDbStorage.getFriends(testUser.getId());
        assertThat(friends)
                .noneMatch(user -> user.getId().equals(friendUser.getId()));
    }

    @Test
    public void testGetFriends() {
        userDbStorage.addFriends(testUser.getId(), friendUser.getId());
        userDbStorage.confirmFriends(friendUser.getId(), testUser.getId());

        List<User> friends = userDbStorage.getFriends(testUser.getId());

        assertThat(friends)
                .isNotEmpty()
                .anyMatch(user -> user.getId().equals(friendUser.getId()));
    }

    @Test
    public void testGetFriendRequests() {
        userDbStorage.addFriends(testUser.getId(), friendUser.getId());

        List<User> requests = userDbStorage.getFriendRequests(friendUser.getId());

        assertThat(requests)
                .isNotEmpty()
                .anyMatch(user -> user.getId().equals(testUser.getId()));
    }

    @Test
    public void testGetCommonFriends() {
        User thirdUser = new User();
        thirdUser.setEmail("third" + System.currentTimeMillis() + "@test.com");
        thirdUser.setLogin("third" + System.currentTimeMillis());
        thirdUser.setName("Third User");
        thirdUser.setBirthday(LocalDate.of(1988, 12, 10));
        final User createdThirdUser = userDbStorage.createUser(thirdUser);

        userDbStorage.addFriends(testUser.getId(), createdThirdUser.getId());
        userDbStorage.confirmFriends(createdThirdUser.getId(), testUser.getId());

        userDbStorage.addFriends(friendUser.getId(), createdThirdUser.getId());
        userDbStorage.confirmFriends(createdThirdUser.getId(), friendUser.getId());

        List<User> commonFriends = userDbStorage.getCommonFriends(testUser.getId(), friendUser.getId());

        assertThat(commonFriends)
                .isNotEmpty()
                .anyMatch(user -> user.getId().equals(createdThirdUser.getId()));
    }
}