package com.metaverse.msme.msme_unit_details.controller;

import com.metaverse.msme.common.ApplicationAPIResponse;
import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.msme_unit_details.service.MsmeUnitDetailsDto;
import com.metaverse.msme.msme_unit_details.service.MsmeUnitDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/msme-unit")
@RequiredArgsConstructor
public class MsmeUnitDetailsController {

    private final MsmeUnitDetailsService msmeUnitDetailsService;

    @PutMapping("/{msmeUnitId}")
    public ResponseEntity<ApplicationAPIResponse<MsmeUnitDetailsDto>> updateMsmeUnitDetails(
            @PathVariable Long msmeUnitId,
            @RequestBody MsmeUnitDetailsDto request) {

        MsmeUnitDetailsDto updated =
                msmeUnitDetailsService.updateMsmeUnitDetails(msmeUnitId, request);

        return ResponseEntity.ok(
                ApplicationAPIResponse.<MsmeUnitDetailsDto>builder()
                        .data(updated)
                        .code(HttpStatus.OK.value())
                        .message("MSME Unit updated successfully")
                        .success(true)
                        .build()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApplicationAPIResponse<MsmeUnitDetailsDto>> getMsmeUnit(@PathVariable Long id) {

        MsmeUnitDetailsDto dto = msmeUnitDetailsService.getMsmeUnitById(id);

        ApplicationAPIResponse<MsmeUnitDetailsDto> response =
                ApplicationAPIResponse.<MsmeUnitDetailsDto>builder()
                        .data(dto)
                        .code(HttpStatus.OK.value())
                        .message("MSME Unit fetched successfully")
                        .success(true)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }


}
