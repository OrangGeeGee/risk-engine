package lt.asinica.riskengine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No user with specified userId found.")
public class UserDoesNotExist extends RuntimeException {
}
