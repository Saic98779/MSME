package com.metaverse.msme.files.controller;

import com.metaverse.msme.files.service.FileService;
import lombok.RequiredArgsConstructor;
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
    public String uploadFile(@RequestParam("file") MultipartFile file,@RequestParam String msmeUnitId) {
        return fileService.uploadFile(file, msmeUnitId);
    }

    @GetMapping
    public List<String> listFiles() {
        return fileService.listFiles();
    }

    @PutMapping(params = "path", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String replaceFile(@RequestParam String path, @RequestParam("file") MultipartFile file) {
        return fileService.replaceFile(path, file);
    }

    @DeleteMapping(params = "path")
    public ResponseEntity<Void> deleteFile(@RequestParam String path) {
        boolean removed = fileService.deleteFile(path);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
