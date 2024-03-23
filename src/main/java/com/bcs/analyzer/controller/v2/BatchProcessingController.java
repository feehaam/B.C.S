package com.bcs.analyzer.controller.v2;

import com.bcs.analyzer.model.UnifiedDTO;
import com.bcs.analyzer.service.v2.BatchProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchProcessingController {

    private final BatchProcessingService bpService;

    @PostMapping("/mcq")
    public ResponseEntity<?> createMCQBatch(@RequestBody List<UnifiedDTO> mcqdtoList) {
        return ResponseEntity.ok(bpService.createBatchMCQ(mcqdtoList));
    }

    @PostMapping("/tag-and-ban")
    public ResponseEntity<?> caretTagBatch(@RequestBody UnifiedDTO unifiedDTO){
        return ResponseEntity.ok(bpService.createAndGetTagAndBanBatch(unifiedDTO));
    }
}
