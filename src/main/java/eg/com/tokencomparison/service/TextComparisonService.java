package eg.com.tokencomparison.service;

import eg.com.tokencomparison.dto.ComparisonResult;
import eg.com.tokencomparison.enums.FileComparisonError;
import eg.com.tokencomparison.exception.custom.FileComparisonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@Service
public class TextComparisonService {

    @Value("${app.comparison.folder-path}")
    private String folderPath;

    public ComparisonResult compare(MultipartFile request) throws FileComparisonException {
        try {
            return compareFiles(request);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new FileComparisonException(FileComparisonError.FILE_CONVERSION_FAILED, e);
        }
    }

    private ComparisonResult compareFiles(MultipartFile referenceFile) throws IOException, ExecutionException, InterruptedException {
        File tempReferenceFile = convert(referenceFile);

        File folder = validateFolderExists();

        List<File> textFiles = getTextFilesFromFolder(folder);

        List<File> filesToCompare = new ArrayList<>();
        filesToCompare.add(tempReferenceFile);
        filesToCompare.addAll(textFiles);

        Map<String, Set<String>> tokenMap = tokenizeAll(filesToCompare);

        Map<String, String> comparisons = compareWithReference(tokenMap, tempReferenceFile.getName());

        cleanupTempFile(tempReferenceFile);

        return new ComparisonResult(tempReferenceFile.getName(), comparisons);
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new FileComparisonException(FileComparisonError.EMPTY_REFERENCE_FILE);
        }

        File tempFile = File.createTempFile("reference -", ".txt");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    private File validateFolderExists() {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            throw new FileComparisonException(FileComparisonError.FOLDER_NOT_FOUND);
        }
        if (!folder.isDirectory()) {
            throw new FileComparisonException(FileComparisonError.FOLDER_NOT_FOUND);
        }
        return folder;
    }

    private List<File> getTextFilesFromFolder(File folder) {
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files == null || files.length == 0) {
            throw new FileComparisonException(FileComparisonError.NO_TEXT_FILES);
        }
        return Arrays.asList(files);
    }

    private Map<String, Set<String>> tokenizeAll(List<File> files) throws IOException, ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Map<String, Set<String>> tokenMap = new ConcurrentHashMap<>();

        List<Future<Void>> futures = new ArrayList<>();

        for (File file : files) {
            final File f = file;
            futures.add(executor.submit(() -> {
                try {
                    Set<String> tokens = tokenize(f);
                    tokenMap.put(f.getName(), tokens);
                } catch (IOException e) {
                    throw new FileComparisonException(FileComparisonError.FILE_CONVERSION_FAILED, e);
                }
                return null;
            }));
        }

        for (Future<Void> future : futures) {
            future.get();
        }

        executor.shutdown();
        return tokenMap;
    }

    private Set<String> tokenize(File file) throws IOException {
        Set<String> tokens = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Arrays.stream(line.toLowerCase().split("\\W+"))
                        .filter(token -> !token.isEmpty())
                        .forEach(tokens::add);
            }
        }
        return tokens;
    }

    private Map<String, String> compareWithReference(Map<String, Set<String>> tokenMap, String referenceFileName) {
        Set<String> referenceTokens = tokenMap.get(referenceFileName);
        if (referenceTokens == null) {
            throw new FileComparisonException(FileComparisonError.FILE_CONVERSION_FAILED);
        }

        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : tokenMap.entrySet()) {
            if (!entry.getKey().equals(referenceFileName)) {
                result.put(entry.getKey(), jaccardSimilarity(referenceTokens, entry.getValue()) );
            }
        }

        return result;
    }

    private String jaccardSimilarity(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return  String.format("%.2f", 100 * (double) intersection.size() / union.size())+" %" ;
    }

    private void cleanupTempFile(File file) {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Give time for read
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new FileComparisonException(FileComparisonError.FILE_CONVERSION_FAILED, e);
            }
            if (!file.delete()) {
                throw new FileComparisonException(FileComparisonError.FILE_CONVERSION_FAILED);
            }
        }).start();
    }
}