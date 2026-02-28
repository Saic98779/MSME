package com.metaverse.msme.msme_unit_details.controller;

import com.metaverse.msme.common.ApplicationAPIResponse;
import com.metaverse.msme.msme_unit_details.service.MsmeUnitDetailsDto;
import com.metaverse.msme.msme_unit_details.service.MsmeUnitDetailsService;
import com.metaverse.msme.msme_unit_details.service.MsmeUnitSearchPageResponse;
import com.metaverse.msme.msme_unit_details.service.MsmeUnitSearchRequest;
import com.metaverse.msme.msme_unit_details.service.MsmeUnitSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/msme-unit")
@RequiredArgsConstructor
@Tag(name = "MSME Unit Details", description = "MSME Unit management and search APIs")
public class MsmeUnitDetailsController {

    private final MsmeUnitDetailsService msmeUnitDetailsService;

    @PutMapping("/update")
    @Operation(
            summary = "Update MSME unit details",
            description = "Update existing MSME unit information by unit ID"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateMsmeUnitDetails(
            @Parameter(description = "MSME Unit ID") @RequestParam(required = false) Long msmeUnitId,
            @RequestBody MsmeUnitDetailsDto request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        if (userId == null || "anonymousUser".equals(userId)) {
            ApplicationAPIResponse<String> response = ApplicationAPIResponse.<String>builder()
                    .success(false)
                    .message("User authentication required")
                    .code(401)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        MsmeUnitDetailsDto updated = msmeUnitDetailsService.updateMsmeUnitDetails(msmeUnitId, request, userId);

        ApplicationAPIResponse<MsmeUnitDetailsDto> response = ApplicationAPIResponse.<MsmeUnitDetailsDto>builder()
                .data(updated)
                .success(true)
                .message("MSME unit details updated successfully")
                .code(200)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get MSME unit by ID",
            description = "Retrieve MSME unit details by unit ID"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getMsmeUnit(
            @Parameter(description = "MSME Unit ID") @PathVariable Long id) {

        MsmeUnitDetailsDto unitDetails = msmeUnitDetailsService.getMsmeUnitById(id);

        ApplicationAPIResponse<MsmeUnitDetailsDto> response = ApplicationAPIResponse.<MsmeUnitDetailsDto>builder()
                .data(unitDetails)
                .success(true)
                .message("MSME unit details retrieved successfully")
                .code(200)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    @Operation(
            summary = "Search MSME units",
            description = "Search MSME units with flexible criteria: districts, mandals, villages, unit name, or mobile number. " +
                    "Supports multiple combinations and partial matching for text fields.")
    public ResponseEntity<?> searchMsmeUnits(
            @Parameter(description = "Page number (0-based). Overrides body.page if provided.")
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Search criteria. All fields are optional. Can combine multiple filters.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MsmeUnitSearchRequest.class)))
            @RequestBody MsmeUnitSearchRequest request) {

        Page<MsmeUnitSearchResponse> results = msmeUnitDetailsService.searchMsmeUnits(request);

        MsmeUnitSearchPageResponse pageResponse = MsmeUnitSearchPageResponse.builder()
                .content(results.getContent())
                .pageNumber(results.getNumber())
                .pageSize(results.getSize())
                .totalElements(results.getTotalElements())
                .totalPages(results.getTotalPages())
                .build();

        ApplicationAPIResponse<MsmeUnitSearchPageResponse> response =
                ApplicationAPIResponse.<MsmeUnitSearchPageResponse>builder()
                        .data(pageResponse)
                        .success(true)
                        .message(results.getTotalElements() + " units found")
                        .code(200)
                        .build();

        return ResponseEntity.ok(response);
    }
}
