package com.metaverse.msme.controller;

import com.metaverse.msme.extractor.MsmeExcelService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/msme")
public class MsmeExcelController {

    private final MsmeExcelService excelService;

    public MsmeExcelController(MsmeExcelService excelService) {
        this.excelService = excelService;
    }

    @GetMapping("/download-excel")
    public ResponseEntity<byte[]> downloadExcel(@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "10") int end) {

        byte[] excel = null;
        try {
            excel = excelService.generateExcel(start,end);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=msme_details.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
    }

    @PostMapping(
            value = "/import-sangareddy-csv",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> importCsv(
            @RequestPart("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Please upload a CSV file");
        }

        excelService.importCsv(file);
        return ResponseEntity.accepted()
                .body("CSV import started successfully");
    }

}
