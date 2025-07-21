package eg.com.tokencomparison.dto;

import java.util.Map;

public class ComparisonResult {
    private final String referenceFile;
    private final Map<String, String> comparisons;

    public ComparisonResult(String referenceFile, Map<String, String> comparisons) {
        this.referenceFile = referenceFile;
        this.comparisons = comparisons;
    }

    public String getReferenceFile() {
        return referenceFile;
    }

    public Map<String, String> getComparisons() {
        return comparisons;
    }
}