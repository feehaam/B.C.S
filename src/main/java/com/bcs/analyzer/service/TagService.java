package com.bcs.analyzer.service;

import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service @RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag getTagById(Integer id){
        Optional<Tag> tag = tagRepository.findById(id);
        return tag.orElse(null);
    }

    public List<Tag> getAllTags(){
        return tagRepository.findAll();
    }

    public Tag create(String word){
        word = word.toLowerCase();
        return tagRepository.save(Tag.builder().questions(new ArrayList<>()).id(0).word(word).build());
    }

    public Tag update(Integer id, String word){
        Optional<Tag> tagOp = tagRepository.findById(id);
        if(tagOp.isEmpty()) return null;
        Tag tag = tagOp.get();
        tag.setWord(word);
        return tagRepository.save(tag);
    }

    public void delete(Integer id){
        tagRepository.deleteById(id);
    }
}
