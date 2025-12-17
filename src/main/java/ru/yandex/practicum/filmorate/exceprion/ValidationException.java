package ru.yandex.practicum.filmorate.exceprion;

import org.springframework.http.HttpStatus;

public class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
