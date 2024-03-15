package com.bcs.analyzer.controller;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.MCQDTO;
import com.bcs.analyzer.service.MCQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mcqs")
public class MCQController {

    @Autowired
    private MCQService mcqService;

    @GetMapping("/{id}")
    public ResponseEntity<MCQ> getMCQById(@PathVariable Integer id) {
        MCQ mcq = mcqService.getMCQById(id);
        if (mcq == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mcq);
    }

    @GetMapping
    public ResponseEntity<List<MCQ>> getByFilter(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String[] tags) {
        List<MCQ> mcqs = mcqService.getByFilter(pageNo, pageSize, year, subject, search, tags);
        return ResponseEntity.ok(mcqs);
    }

    @PostMapping
    public ResponseEntity<MCQ> createMCQ(@RequestBody MCQDTO mcqdto) {
        MCQ mcq = mcqService.create(mcqdto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mcq);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MCQ> updateMCQ(@PathVariable Integer id, @RequestBody MCQDTO mcqdto) {
        MCQ updatedMCQ = mcqService.update(id, mcqdto);
        if (updatedMCQ == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedMCQ);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMCQ(@PathVariable Integer id) {
        mcqService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}