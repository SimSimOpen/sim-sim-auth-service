package info.jemsit.auth_service.exception;

import info.jemsit.common.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Advice {
    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> handleException(UserException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error " + e.getMessage());
    }
}
