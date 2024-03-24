package com.bcs.analyzer.service.v1;

import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.repository.TagRepository;
import com.bcs.analyzer.util.Cache;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service @RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @PostConstruct
    private void loadTags(){
        reloadTags();
    }

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

    public List<Tag> createBatch(List<String> tagWords) {
        List<Tag> existingTags = tagRepository.findAllByWords(tagWords);
        List<Tag> newTags = new ArrayList<>();
        tagWords.forEach(word -> {
            boolean doesNotExist = true;
            for(Tag et: existingTags){
                if(et.getWord().equals(word.toLowerCase())){
                    doesNotExist = false;
                    break;
                }
            }
            if(doesNotExist) newTags.add(new Tag(0, word, new ArrayList<>()));
        });
        tagRepository.saveAll(newTags);
        Set<Tag> tagsSet = new HashSet<>(tagRepository.findAllByWords(tagWords));
        return new ArrayList<>(tagsSet);
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
        Cache.setAllTags(tagRepository.findAll());
    }
}
