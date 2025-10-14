package cat.itacademy.s04.t02.n01.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
    Map<String, String> body = new HashMap<>();
    body.put("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler({IllegalArgumentException.class, ConstraintViolationException.class, MethodArgumentNotValidException.class})
  public ResponseEntity<?> handleBadRequest(Exception ex) {
    Map<String, String> body = new HashMap<>();
    body.put("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    Map<String, String> body = new HashMap<>();
    body.put("error", "Invalid parameter: " + ex.getName());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleOther(Exception ex) {
    Map<String, String> body = new HashMap<>();
    body.put("error", "Internal server error");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }
}
