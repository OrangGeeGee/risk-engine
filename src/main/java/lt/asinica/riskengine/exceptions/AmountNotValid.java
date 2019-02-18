package lt.asinica.riskengine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Amount not valid.")
public class AmountNotValid extends RuntimeException {
}
