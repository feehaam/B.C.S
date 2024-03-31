package com.bcs.analyzer.controller.v1;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.MCQDTO;
import com.bcs.analyzer.service.v1.MCQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class MCQController {

    @Autowired
    private MCQService mcqService;

    @GetMapping("/{id}")
    public ResponseEntity<MCQ> getMCQById(@PathVariable("id") Integer id) {
        MCQ mcq = mcqService.getMCQById(id);
        if (mcq == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mcq);
    }

    @PostMapping
    public ResponseEntity<MCQ> createMCQ(@RequestBody MCQDTO mcqdto) {
        MCQ mcq = mcqService.create(mcqdto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mcq);
    }

    @PostMapping("/batch")
    public ResponseEntity<?> createMCQBatch(@RequestBody List<MCQDTO> mcqdtoList) {
        return ResponseEntity.ok(mcqService.createMCQBatch(mcqdtoList));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MCQ> updateMCQ(@PathVariable("id") Integer id, @RequestBody MCQDTO mcqdto) {
        MCQ updatedMCQ = mcqService.update(id, mcqdto);
        if (updatedMCQ == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedMCQ);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMCQ(@PathVariable("id") Integer id) {
        mcqService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}