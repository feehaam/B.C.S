package com.bcs.analyzer.controller;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.UnifiedDTO;
import com.bcs.analyzer.service.UnifiedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unified")
@RequiredArgsConstructor
public class UnifiedController {

    private final UnifiedService unifiedService;

    @PostMapping
    public ResponseEntity<?> createMCQBatch(@RequestBody List<UnifiedDTO> mcqdtoList) {
        return ResponseEntity.ok(unifiedService.createBatchMCQ(mcqdtoList));
    }

    @GetMapping
    public ResponseEntity<?> getByFilter(
            @RequestParam(required = false, name = "pageNo") Integer pageNo,
            @RequestParam(required = false, name = "pageSize") Integer pageSize,
            @RequestParam(required = false, name = "year") Integer year,
            @RequestParam(required = false, name = "subject") String subject,
            @RequestParam(required = false, name = "search") String search,
            @RequestParam(required = false, name = "tags") String[] tags) {
        return ResponseEntity.ok(unifiedService.getByFilter(pageNo, pageSize, year, subject, search, tags));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MCQ> setBanAndTags(@PathVariable("id") Integer id, @RequestBody UnifiedDTO unifiedDTO) {
        MCQ updatedMCQ = unifiedService.setBanAndTags(id, unifiedDTO);
        if (updatedMCQ == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedMCQ);
    }
}