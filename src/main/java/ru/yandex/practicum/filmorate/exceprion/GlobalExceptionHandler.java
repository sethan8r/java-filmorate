package ru.yandex.practicum.filmorate.exceprion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException ex) {
        log.warn("Ошибка бизнес логики: {}", ex.getMessage());

        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }
}
