package com.bcs.analyzer.controller;

import com.bcs.analyzer.model.MCQDTO;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/util")
@RequiredArgsConstructor
public class UtilityController {

    private final TagService tagService;

    @GetMapping("/suggest-tags")
    public ResponseEntity<List<Tag>> getSuggestedTags(MCQDTO mcqdto){
        return ResponseEntity.ok(tagService.getSuggestedTags(mcqdto));
    }

    public void getSuggestedMCQ(String text){

    }

    public void reloadSimilarity(){

    }

}
