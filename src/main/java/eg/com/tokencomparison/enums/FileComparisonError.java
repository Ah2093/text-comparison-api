package eg.com.tokencomparison.enums;

public enum FileComparisonError {
    FOLDER_NOT_FOUND("Folder does not exist or is not a directory"),
    NO_TEXT_FILES("No .txt files found in folder"),
    FILE_CONVERSION_FAILED("Error converting uploaded file"),
    EMPTY_REFERENCE_FILE("Reference file is empty");

    private final String message;

    FileComparisonError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}