package com.bcs.analyzer.controller.v1;

import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.service.v1.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getTagById(@PathVariable("id") Integer id) {
        Tag tag = tagService.getTagById(id);
        if (tag == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tag);
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody String word) {
        Tag tag = tagService.create(word);
        return ResponseEntity.ok(tag);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable("id") Integer id, @RequestBody String word) {
        Tag updatedTag = tagService.update(id, word);
        if (updatedTag == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable("id") Integer id) {
        tagService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Tag deleted successfully");
    }
}