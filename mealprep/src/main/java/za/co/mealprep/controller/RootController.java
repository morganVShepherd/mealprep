package za.co.mealprep.controller;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import za.co.mealprep.exception.ErrorConstants;

import java.util.HashMap;
import java.util.Map;

public class RootController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = this.formatErrorMessage(error.getDefaultMessage());
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    private String formatErrorMessage(String errorMessage) {
        switch (errorMessage) {
            case "size":
                return ErrorConstants.INCORRECT_LENGTH;
            case "notNull":
            case "notEmpty":
                return ErrorConstants.CANNOT_BE_EMPTY;
            case "positive":
                return ErrorConstants.MUST_BE_POSITIVE;
            default:
                return errorMessage;
        }
    }
}
