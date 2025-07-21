package eg.com.tokencomparison.error;

import eg.com.tokencomparison.enums.FileComparisonError;

public class ErrorResponse {
    private final String message;

    public ErrorResponse(FileComparisonError error) {
        this.message = error.getMessage();
    }

    public String getMessage() {
        return message;
    }
}