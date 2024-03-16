package com.bcs.analyzer.service;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.MCQDTO;
import com.bcs.analyzer.model.PendingAnalyzer;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.repository.MCQRepository;
import com.bcs.analyzer.repository.PARepository;
import com.bcs.analyzer.repository.TagRepository;
import com.bcs.analyzer.util.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class MCQService extends MCQFormatter{
    private static final Integer ANALYZER_OPERATION_TYPE = 1;

    private final MCQRepository mcqRepository;
    private final TagRepository tagRepository;
    private final PARepository paRepository;

    public MCQ getMCQById(Integer id){
        Optional<MCQ> mcq = mcqRepository.findById(id);
        return mcq.orElse(null);
    }

    public List<MCQ> getByFilter(Integer pageNo, Integer pageSize, Integer year, String subject, String search, String... tags) {
        int pageNumber = pageNo != null ? pageNo - 1 : 0;
        List<String> tagsList = tags != null ?
                tagRepository.findAllByIds(Arrays
                                .stream(tags)
                                .map(Integer::parseInt)
                                .toList())
                        .stream()
                        .map(Tag::getWord)
                        .toList()
                : Collections.emptyList();
        boolean allFiltersNull = year == null && subject == null && search == null && tagsList.isEmpty();
        if (allFiltersNull) {
            return mcqRepository.findAll(PageRequest.of(pageNo, pageSize)).getContent();
        } else {
            return mcqRepository.findAllByFilters(year, subject, search, tagsList.isEmpty() ? null : tagsList);
        }
    }

    public MCQ create(MCQDTO mcqdto){
        List<Tag> tags = tagRepository.findAllByWords(mcqdto.getTagWords());
        MCQ mcq = MCQ.builder()
                .question(mcqdto.getQuestion())
                .optionA(mcqdto.getOptionA())
                .optionB(mcqdto.getOptionB())
                .optionC(mcqdto.getOptionC())
                .optionD(mcqdto.getOptionD())
                .answer(mcqdto.getAnswer())
                .subject(mcqdto.getSubject())
                .year(mcqdto.getYear())
                .tags(tags)
                .similarity(0)
                .build();
        var result = mcqRepository.save(mcq);
        updatePendingAnalyzer(result.getId());
        updateRecentTags(tags);
        return result;
    }

    public MCQ update(Integer id, MCQDTO mcqdto){
        Optional<MCQ> mcqOp = mcqRepository.findById(id);
        if(mcqOp.isEmpty()) return null;
        MCQ mcq = mcqOp.get();
        List<Tag> tags = tagRepository.findAllByWords(mcqdto.getTagWords());
        MCQ mcqUpdated = MCQ.builder()
                .id(id)
                .question(mcqdto.getQuestion())
                .optionA(mcqdto.getOptionA())
                .optionB(mcqdto.getOptionB())
                .optionC(mcqdto.getOptionC())
                .optionD(mcqdto.getOptionD())
                .answer(mcqdto.getAnswer())
                .subject(mcqdto.getSubject())
                .year(mcqdto.getYear())
                .tags(tags)
                .similarity(0)
                .build();
        var result = mcqRepository.save(mcqUpdated);
        updateRecentTags(tags);
        return result;
    }

    public void delete(Integer id){
        mcqRepository.deleteById(id);
    }

    private void updateRecentTags(List<Tag> tags){
        Cache.setRecentTags(tags);
    }

    private void updatePendingAnalyzer(Integer targetId) {
        paRepository.save(new PendingAnalyzer(0, targetId, ANALYZER_OPERATION_TYPE));
    }
}
