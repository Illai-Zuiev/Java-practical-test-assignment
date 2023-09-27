package clear.solutions.demo.exception.handler;

import clear.solutions.demo.exception.UserAgeRestrictionException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundExceptions(Exception e, ServletWebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(logAndGetErrorMessage(request, e.getLocalizedMessage(), e));
    }

    @ExceptionHandler(UserAgeRestrictionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageResponse handleBadRequestException(RuntimeException e, ServletWebRequest request) {
        return logAndGetErrorMessage(request, e.getLocalizedMessage(), e);
    }

    private ErrorMessageResponse logAndGetErrorMessage(ServletWebRequest request, String message, Exception e) {
        var errorMessage = new ErrorMessageResponse(message, request.getRequest().getRequestURI(), request.getHttpMethod().name());
        return errorMessage;
    }
}
