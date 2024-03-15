package com.bcs.analyzer.service;

import com.bcs.analyzer.model.Ban;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.repository.TagRepository;
import com.bcs.analyzer.util.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Tag result = tagRepository.save(Tag.builder().questions(new ArrayList<>()).id(0).word(word).build());
        reloadTags();
        return result;
    }

    public Tag update(Integer id, String word){
        Optional<Tag> tagOp = tagRepository.findById(id);
        if(tagOp.isEmpty()) return null;
        Tag tag = tagOp.get();
        tag.setWord(word);
        Tag result = tagRepository.save(tag);
        reloadTags();
        return  result;
    }

    public void delete(Integer id){
        tagRepository.deleteById(id);
        reloadTags();
    }

    private void reloadTags(){
        Cache.setAllTags(tagRepository
                .findAll()
                .stream()
                .map(Tag::getWord)
                .collect(Collectors.toList()));
    }
}
