package com.metaverse.msme.msme_unit_details.controller;

import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.msme_unit_details.service.MsmeUnitDetailsDto;
import com.metaverse.msme.msme_unit_details.service.MsmeUnitDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/msme-unit")
@RequiredArgsConstructor
public class MsmeUnitDetailsController {

    private final MsmeUnitDetailsService msmeUnitDetailsService;

    @PutMapping("/{msmeUnitId}")
    public ResponseEntity<MsmeUnitDetails> updateMsmeUnitDetails(@PathVariable Long msmeUnitId,
                                                                 @RequestBody MsmeUnitDetailsDto request) {
        MsmeUnitDetails updated = msmeUnitDetailsService.updateMsmeUnitDetails(msmeUnitId, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MsmeUnitDetailsDto> getMsmeUnit(@PathVariable Long id) {
        return ResponseEntity.ok(msmeUnitDetailsService.getMsmeUnitById(id));
    }

}
