package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends BusinessException {
    public InternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
