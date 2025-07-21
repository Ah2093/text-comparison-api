package eg.com.tokencomparison.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class TextFileValidator implements ConstraintValidator<ValidTextFile, MultipartFile> {

    private static final long MAX_SIZE = 10 * 1024 * 1024; // 10MB

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File cannot be null").addConstraintViolation();
            return false;
        }

        if (file.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File is empty").addConstraintViolation();
            return false;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".txt")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Only .txt files are allowed").addConstraintViolation();
            return false;
        }

        if (file.getSize() > MAX_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size exceeds 10MB").addConstraintViolation();
            return false;
        }

        return true;
    }
}