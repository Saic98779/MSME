package com.metaverse.msme.files.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileService {

    private static final Path UPLOAD_DIR = Paths.get("/var/www/uploads");
    private static final String BASE_URL = "http://51.222.155.92/uploads";

    public String uploadFile(MultipartFile file, String msmeUnitId) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }

        String fileName = generateTargetName(file);
        return storeWithName(fileName, file, msmeUnitId);
    }

    public List<String> listFiles() {
        try {
            ensureUploadDir();

            try (Stream<Path> stream = Files.walk(UPLOAD_DIR)) {
                return stream
                        .filter(Files::isRegularFile)
                        .map(path -> BASE_URL + "/" + UPLOAD_DIR.relativize(path).toString())
                        .sorted()
                        .toList();
            }

        } catch (IOException e) {
            throw new IllegalStateException("Failed to list files", e);
        }
    }

    public boolean deleteFile(String identifier) {
        try {
            ensureUploadDir();
            Path target = resolvePath(UPLOAD_DIR, identifier);
            return Files.deleteIfExists(target);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to delete file", e);
        }
    }

    private String storeWithName(String filename, MultipartFile file, String msmeUnitId) {

        try {
            ensureUploadDir(); // ensure /var/www/uploads exists

            Path tenantDir = msmeUploadDir(msmeUnitId);
            Files.createDirectories(tenantDir);

            Path target = tenantDir.resolve(filename).normalize();

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // return public URL
            return BASE_URL + "/" + msmeUnitId + "/" + filename;

        } catch (IOException e) {
            throw new IllegalStateException("Failed to store file", e);
        }
    }

    private Path resolvePath(Path baseDir, String identifier) {

        String normalized = normalizeIdentifier(identifier);
        Path candidate = baseDir.resolve(normalized).normalize();

        if (!candidate.startsWith(baseDir)) {
            throw new IllegalArgumentException("Invalid file path");
        }

        return candidate;
    }

    private void ensureUploadDir() throws IOException {
        Files.createDirectories(UPLOAD_DIR);
    }

    private Path msmeUploadDir(String msmeUnitId) {

        if (!StringUtils.hasText(msmeUnitId)) {
            throw new IllegalArgumentException("msmeUnitId is required");
        }

        String cleaned = StringUtils.cleanPath(msmeUnitId.trim());

        if (cleaned.contains("..") || cleaned.contains("/") || cleaned.contains("\\")) {
            throw new IllegalArgumentException("Invalid msmeUnitId");
        }

        return UPLOAD_DIR.resolve(cleaned);
    }

    private String normalizeIdentifier(String identifier) {

        if (!StringUtils.hasText(identifier)) {
            throw new IllegalArgumentException("Invalid file identifier");
        }

        return StringUtils.cleanPath(identifier.trim());
    }

    private String generateTargetName(MultipartFile file) {

        String originalName = file.getOriginalFilename();

        if (!StringUtils.hasText(originalName)) {
            return "file";
        }

        return StringUtils.cleanPath(originalName);
    }
}