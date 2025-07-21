package eg.com.tokencomparison.controller;

import eg.com.tokencomparison.dto.ComparisonResult;
import eg.com.tokencomparison.exception.custom.FileComparisonException;
import eg.com.tokencomparison.service.TextComparisonService;
import eg.com.tokencomparison.validation.ValidTextFile;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/text-comparison")
@Validated
public class TextComparisonController {

    private final TextComparisonService textComparisonService;

    public TextComparisonController(TextComparisonService textComparisonService) {
        this.textComparisonService = textComparisonService;
    }

    @PostMapping("/compare")
    public ResponseEntity<ComparisonResult> compareFiles(@Valid @ValidTextFile MultipartFile referenceFile) {
        try {
            return ResponseEntity.ok(textComparisonService.compare(referenceFile));
        } catch (FileComparisonException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}