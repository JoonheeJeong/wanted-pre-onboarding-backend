package wanted.preonboarding.global.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wanted.preonboarding.global.response.ApiMessageResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static ResponseEntity<?> respondAsInvalidInput() {
        return ApiMessageResponse.of(HttpStatus.BAD_REQUEST, "입력이 유효하지 않습니다.");
    }

    @ExceptionHandler
    public ResponseEntity<?> handleJSONException(InvalidFormatException e) {
        return respondAsInvalidInput();
    }

    @ExceptionHandler
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return respondAsInvalidInput();
    }

    @ExceptionHandler
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ApiMessageResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
