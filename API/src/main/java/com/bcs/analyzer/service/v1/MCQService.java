package com.bcs.analyzer.service.v1;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.MCQDTO;
import com.bcs.analyzer.model.PendingAnalyzer;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.repository.MCQRepository;
import com.bcs.analyzer.repository.PARepository;
import com.bcs.analyzer.repository.TagRepository;
import com.bcs.analyzer.util.MCQCache;
import com.bcs.analyzer.util.TagsAndBanCache;
import com.bcs.analyzer.util.ID;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

import static com.bcs.analyzer.util.TagsAndBanCache.subjects;

@Service @RequiredArgsConstructor
public class MCQService extends MCQFormatter{
    private static final Integer ANALYZER_OPERATION_TYPE = 1;

    private final MCQRepository mcqRepository;
    private final MCQCache mcqCache;
    private final TagRepository tagRepository;
    private final PARepository paRepository;
    private final BanService banService;
    private final TagService tagService;
    private final ID id;

    @PostConstruct
    private void loadTags(){
        setSubjectAndTags();
    }

    public MCQ getMCQById(Integer id){
        Optional<MCQ> mcq = mcqRepository.findById(id);
        return mcq.orElse(null);
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

    public ResponseEntity<?> createMCQBatch(@RequestBody List<MCQDTO> mcqdtoList) {
        List<MCQ> mcqList = new ArrayList<>();
        for(MCQDTO mcqdto: mcqdtoList){
            MCQ mcq = MCQ.builder()
                    .question(mcqdto.getQuestion())
                    .optionA(mcqdto.getOptionA())
                    .optionB(mcqdto.getOptionB())
                    .optionC(mcqdto.getOptionC())
                    .optionD(mcqdto.getOptionD())
                    .answer(mcqdto.getAnswer())
                    .year(mcqdto.getYear())
                    .similarity(0)
                    .build();
            mcqList.add(mcq);
        }
        List<MCQ> results = mcqRepository.saveAll(mcqList);

        List<Integer> idList = new ArrayList<>();
        results.forEach(mcq -> idList.add(mcq.getId()));
        updatePendingAnalyzerBatch(idList);

        return ResponseEntity.ok(results);
    }

    public MCQ update(Integer id, MCQDTO mcqdto){
        Optional<MCQ> mcqOp = mcqRepository.findById(id);
        if(mcqOp.isEmpty()) return null;
        MCQ mcq = mcqOp.get();
        List<Tag> tags = tagService.createBatch(mcqdto.getTagWords());
        banService.createBatch(mcqdto.getBans());
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
        TagsAndBanCache.setRecentTags(tags);
    }

    private void updatePendingAnalyzer(Integer targetId) {
        paRepository.save(new PendingAnalyzer(0, targetId, ANALYZER_OPERATION_TYPE, 0));
    }

    private void updatePendingAnalyzerBatch(List<Integer> targetIds) {
        List<PendingAnalyzer> pendingAnalyzers = new ArrayList<>();
        targetIds.forEach(tid -> pendingAnalyzers.add(new PendingAnalyzer(0, tid, ANALYZER_OPERATION_TYPE, 0)));
        paRepository.saveAll(pendingAnalyzers);
    }

    public void setSubjectAndTags() {
        List<MCQ> allMCQ = mcqCache.getAll();
        allMCQ.forEach(mcq -> {
            id.lastMCQId = Math.max(id.lastMCQId, mcq.getId());
            final String subject = mcq.getSubject();
            List<Tag> tags = mcq.getTags();
            if(tags == null) tags = new ArrayList<>();
            subjects.add(subject);
            tags.forEach(tag -> {
                TagsAndBanCache.setAllSubjectBasedTags(subject, tag);
            });
        });
    }
}
