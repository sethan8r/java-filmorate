package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserRequestDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid UserRequestDto request) {
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody @Valid UserRequestDto request) {
        return userService.updateUser(userId, request);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> addFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addFriends(userId, friendId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/friends/{friendId}/confirm")
    public ResponseEntity<Void> confirmFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.confirmFriends(userId, friendId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/friends/{friendId}/reject")
    public ResponseEntity<Void> rejectFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.rejectFriends(userId, friendId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> removeFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.removeFriends(userId, friendId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable Long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.getCommonFriends(userId, friendId);
    }

    @GetMapping("/{userId}/friends/requests")
    public List<User> getFriendRequests(@PathVariable Long userId) {
        return userService.getFriendRequests(userId);
    }
}
