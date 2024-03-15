package com.bcs.analyzer.service;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.MCQDTO;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.repository.MCQRepository;
import com.bcs.analyzer.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service @RequiredArgsConstructor
public class MCQService {

    private final MCQRepository mcqRepository;
    private final TagRepository tagRepository;

    public MCQ getMCQById(Integer id){
        Optional<MCQ> mcq = mcqRepository.findById(id);
        return mcq.orElse(null);
    }

    public List<MCQ> getByFilter(Integer pageNo, Integer pageSize, Integer year, String subject, String search, String... tags) {
        int pageNumber = pageNo != null ? pageNo - 1 : 0;
        List<String> tagsList = tags != null ? List.of(tags) : Collections.emptyList();
        boolean allFiltersNull = year == null && subject == null && search == null && tagsList.isEmpty();
        if (allFiltersNull) {
            return mcqRepository.findAll(PageRequest.of(pageNo, pageSize)).getContent();
        } else {
            return mcqRepository.findAllByFilters(year, subject, search, tagsList);
        }
    }

    public MCQ create(MCQDTO mcqdto){
        List<Tag> tags = tagRepository.findAllByIds(mcqdto.getTagIds());
        MCQ mcq = MCQ.builder()
                .question(mcqdto.getQuestion())
                .optionA(mcqdto.getOptionA())
                .optionB(mcqdto.getOptionB())
                .optionC(mcqdto.getOptionC())
                .optionD(mcqdto.getOptionD())
                .answer(mcqdto.getAnswer())
                .tags(tags)
                .similarity(0)
                .build();
        return mcqRepository.save(mcq);
    }

    public MCQ update(Integer id, MCQDTO mcqdto){
        Optional<MCQ> mcqOp = mcqRepository.findById(id);
        if(mcqOp.isEmpty()) return null;
        MCQ mcq = mcqOp.get();
        List<Tag> tags = tagRepository.findAllByIds(mcqdto.getTagIds());
        MCQ mcqUpdated = MCQ.builder()
                .id(id)
                .question(mcqdto.getQuestion())
                .optionA(mcqdto.getOptionA())
                .optionB(mcqdto.getOptionB())
                .optionC(mcqdto.getOptionC())
                .optionD(mcqdto.getOptionD())
                .answer(mcqdto.getAnswer())
                .tags(tags)
                .similarity(0)
                .build();
        return mcqRepository.save(mcqUpdated);
    }

    public void delete(Integer id){
        mcqRepository.deleteById(id);
    }
}
