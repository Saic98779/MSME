package com.metaverse.msme.files.controller;

import com.metaverse.msme.common.ApplicationAPIResponse;
import com.metaverse.msme.files.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/files")
public class FilesController {

    private final FileService fileService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApplicationAPIResponse<String>> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String msmeUnitId) {
        String storedPath = fileService.uploadFile(file, msmeUnitId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApplicationAPIResponse.<String>builder()
                        .data(storedPath)
                        .message("File uploaded successfully")
                        .code(HttpStatus.CREATED.value())
                        .success(true)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApplicationAPIResponse<List<String>>> listFiles() {
        List<String> files = fileService.listFiles();
        return ResponseEntity.ok(ApplicationAPIResponse.<List<String>>builder()
                .data(files)
                .message("Files fetched")
                .code(HttpStatus.OK.value())
                .success(true)
                .build());
    }

    @PutMapping(params = "path", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApplicationAPIResponse<String>> replaceFile(@RequestParam String path, @RequestParam("file") MultipartFile file) {
        String newPath = fileService.replaceFile(path, file);
        return ResponseEntity.ok(ApplicationAPIResponse.<String>builder()
                .data(newPath)
                .message("File replaced")
                .code(HttpStatus.OK.value())
                .success(true)
                .build());
    }

    @DeleteMapping(params = "path")
    public ResponseEntity<ApplicationAPIResponse<Void>> deleteFile(@RequestParam String path) {
        boolean removed = fileService.deleteFile(path);
        if (removed) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApplicationAPIResponse.<Void>builder()
                            .message("File deleted")
                            .code(HttpStatus.OK.value())
                            .success(true)
                            .build()
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApplicationAPIResponse.<Void>builder()
                        .message("File not found")
                        .code(HttpStatus.NOT_FOUND.value())
                        .success(false)
                        .build()
        );
    }

}
