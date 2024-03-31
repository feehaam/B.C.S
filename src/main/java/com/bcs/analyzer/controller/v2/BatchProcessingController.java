package com.bcs.analyzer.controller.v2;

import com.bcs.analyzer.model.UnifiedDTO;
import com.bcs.analyzer.service.v2.BatchProcessingService;
import com.bcs.analyzer.service.v2.UnifiedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchProcessingController {

    private final BatchProcessingService bpService;
    private final UnifiedService unifiedService;

    @PostMapping("/mcq")
    public ResponseEntity<?> createMCQBatch(@RequestBody List<UnifiedDTO> mcqdtoList) {
        return ResponseEntity.ok(bpService.createBatchMCQ(mcqdtoList));
    }

    @PostMapping("/tag-and-ban")
    public ResponseEntity<?> createTagsBatch(@RequestBody UnifiedDTO unifiedDTO){
        return ResponseEntity.ok(bpService.createAndGetTagAndBanBatch(unifiedDTO));
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingMCQToAnalyze(){
        return ResponseEntity.ok(unifiedService.getPendingMCQToAnalyze());
    }
}
