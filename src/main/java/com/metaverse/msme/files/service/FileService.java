package com.metaverse.msme.files.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileService {



    private static final Path UPLOAD_DIR = Paths.get("E:/uploads");

    public String uploadFile(MultipartFile file, String msmeUnitId) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }
        return storeWithName(generateTargetName(file), file,msmeUnitId);
    }

    public List<String> listFiles() {
        try {
            ensureUploadDir();
            try (Stream<Path> stream = Files.list(UPLOAD_DIR)) {
                return stream
                        .filter(Files::isRegularFile)
                        .map(path -> path.toAbsolutePath().toString())
                        .sorted()
                        .toList();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to list files", e);
        }
    }

    public String replaceFile(String identifier, MultipartFile file) {

        try {
            ensureUploadDir();

            Path oldFile = resolvePath(identifier);
            Files.deleteIfExists(oldFile);


            String newFileName = file.getOriginalFilename();
            Path newPath = UPLOAD_DIR.resolve(newFileName);

            Files.copy(file.getInputStream(), newPath, StandardCopyOption.REPLACE_EXISTING);

            return newPath.toString();

        } catch (IOException e) {
            throw new RuntimeException("File replace failed", e);
        }
    }

    public boolean deleteFile(String identifier) {
        try {
            ensureUploadDir();
            Path target = resolvePath(identifier);
            return Files.deleteIfExists(target);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to delete file", e);
        }
    }

    private String storeWithName(String filename, MultipartFile file,String msmeUnitId) {
        try {
            ensureUploadDir(msmeUnitId);
            Path target = resolvePath(filename);
             boolean b = Files.deleteIfExists(target);// remove old file fully

            Files.write(target, file.getBytes(), StandardOpenOption.CREATE_NEW);
            return target.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store file", e);
        }
    }

    private Path resolvePath(String identifier) {
        String normalized = normalizeIdentifier(identifier);
        Path candidate;
        if (Paths.get(normalized).isAbsolute()) {
            candidate = Paths.get(normalized).normalize();
            return candidate;
        } else {
            candidate = UPLOAD_DIR.resolve(normalized).normalize();
            if (!candidate.startsWith(UPLOAD_DIR)) {
                throw new IllegalArgumentException("Invalid file path");
            }
        }
        return candidate;
    }

    private void ensureUploadDir() throws IOException {
        Files.createDirectories(UPLOAD_DIR);
    }

    private void ensureUploadDir(String msmeUnitId) throws IOException {
        Files.createDirectories(Path.of(UPLOAD_DIR + "/" + msmeUnitId));
    }

    private String normalizeIdentifier(String identifier) {
        if (!StringUtils.hasText(identifier)) {
            throw new IllegalArgumentException("Invalid file identifier");
        }
        String id = StringUtils.trimWhitespace(identifier);

        if (id.contains("://")) {
            try {
                URI uri = URI.create(id);
                String path = uri.getPath();
                if (!StringUtils.hasText(path)) {
                    throw new IllegalArgumentException("Invalid file identifier");
                }
                int slash = path.lastIndexOf('/');
                id = slash >= 0 ? path.substring(slash + 1) : path;
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid file identifier", ex);
            }
        }

        return StringUtils.cleanPath(id);
    }

    private String generateTargetName(MultipartFile file) {
        String originalName = StringUtils.cleanPath(
                file.getOriginalFilename() == null ? "file" : file.getOriginalFilename()
        );

        return originalName;
    }
}
