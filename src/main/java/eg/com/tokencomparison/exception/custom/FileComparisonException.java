package eg.com.tokencomparison.exception.custom;

import eg.com.tokencomparison.enums.FileComparisonError;

public class FileComparisonException extends RuntimeException {
    private final FileComparisonError error;
    private final Throwable cause;

    public FileComparisonException(FileComparisonError error) {
        super(error.getMessage());
        this.error = error;
        this.cause = null;
    }

    public FileComparisonException(FileComparisonError error, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
        this.cause = cause;
    }

    public FileComparisonError getError() {
        return error;
    }

    public Throwable getCause() {
        return cause;
    }
}