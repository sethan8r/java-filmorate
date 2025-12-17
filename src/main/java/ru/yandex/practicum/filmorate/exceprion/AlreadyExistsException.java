package ru.yandex.practicum.filmorate.exceprion;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends BusinessException {
    public AlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
