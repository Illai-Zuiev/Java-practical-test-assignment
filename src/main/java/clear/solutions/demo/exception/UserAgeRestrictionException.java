package clear.solutions.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAgeRestrictionException extends RuntimeException {

    public UserAgeRestrictionException(String message) {
        super(message);
    }
}