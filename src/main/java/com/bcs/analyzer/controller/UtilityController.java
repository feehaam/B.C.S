package com.bcs.analyzer.controller;

import com.bcs.analyzer.model.MCQDTO;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.service.MCQService;
import com.bcs.analyzer.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/util")
@RequiredArgsConstructor
public class UtilityController {

    private final TagService tagService;
    private final MCQService mcqService;

    @GetMapping("/suggest-tags")
    public ResponseEntity<List<Tag>> getSuggestedTags(@RequestBody MCQDTO mcqdto){
        return ResponseEntity.ok(tagService.getSuggestedTags(mcqdto));
    }

    public ResponseEntity<MCQDTO> getSuggestedMCQ(@RequestBody String text){
        return ResponseEntity.ok(mcqService.format(text));
    }

    public ResponseEntity<?> getSuggestedMCQAndTags(@RequestBody String text){
        Map<String, Object> suggestions = new HashMap<>();
        MCQDTO mcqdto = mcqService.format(text);
        suggestions.put("mcq", mcqdto);
        suggestions.put("tags", tagService.getSuggestedTags(mcqdto));
        return ResponseEntity.ok(suggestions);
    }

    public void reloadSimilarity(){

    }

}
