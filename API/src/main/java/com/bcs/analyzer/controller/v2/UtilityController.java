package com.bcs.analyzer.controller.v2;

import com.bcs.analyzer.model.MCQDTO;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.model.UnifiedDTO;
import com.bcs.analyzer.service.v1.MCQService;
import com.bcs.analyzer.service.v1.TagSuggestion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/util")
@RequiredArgsConstructor
public class UtilityController {

    private final TagSuggestion suggester;

    @GetMapping("/suggest-tags/{id}")
    public ResponseEntity<Map<String, List<Tag>>> getSuggestedTags(@PathVariable("id") int mcqId){
        return ResponseEntity.ok(suggester.getSuggestedTags(mcqId));
    }

    public ResponseEntity<MCQDTO> getSuggestedMCQ(@RequestBody String text){
        return null;
    }

    public void reloadSimilarity(){

    }

}
