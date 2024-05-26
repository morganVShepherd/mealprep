package za.co.mealprep.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class RestException extends Exception {
    private Error error;

    public RestException(Error error) {
        super(error.getLogError());
        this.error = error;
    }

    public RestException(String errorMessage, Error error) {
        super(errorMessage);
        this.error = error;
        log.error(error.getLogError());
    }

    public ResponseEntity getRestfulResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(ErrorConstants.ERROR, error.getFrontEndError());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(error.getFrontEndError());
    }

    public Error getError() {
        return error;
    }
}
