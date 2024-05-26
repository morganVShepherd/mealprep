package za.co.mealprep.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {
    private String logError;
    private String frontEndError;
    private int errorCode;
}
