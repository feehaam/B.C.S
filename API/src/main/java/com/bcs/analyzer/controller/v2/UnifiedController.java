package com.bcs.analyzer.controller.v2;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.UnifiedDTO;
import com.bcs.analyzer.service.v2.UnifiedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unified")
@RequiredArgsConstructor
public class UnifiedController {

    private final UnifiedService unifiedService;

    @GetMapping
    public ResponseEntity<?> getByFilter(
            @RequestParam(required = false, name = "mcqId") Integer mcqId,
            @RequestParam(required = false, name = "pageNo") Integer pageNo,
            @RequestParam(required = false, name = "pageSize") Integer pageSize,
            @RequestParam(required = false, name = "year") Integer year,
            @RequestParam(required = false, name = "subject") String subject,
            @RequestParam(required = false, name = "search") String search,
            @RequestParam(required = false, name = "sortBy") String sortBy,
            @RequestParam(required = false, name = "tags") String[] tags) {
        return ResponseEntity.ok(unifiedService.getByFilter(mcqId, pageNo, pageSize, year, subject, search, sortBy, tags));
    }

    @GetMapping("/filters")
    public ResponseEntity<?> getFilters(){
        return ResponseEntity.ok(unifiedService.getFilters());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MCQ> setBanAndTags(@PathVariable("id") Integer id, @RequestBody UnifiedDTO unifiedDTO) {
        MCQ updatedMCQ = unifiedService.setBanAndTags(id, unifiedDTO);
        if (updatedMCQ == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedMCQ);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MCQ> removeTags(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(unifiedService.removeTags(id));
    }
}