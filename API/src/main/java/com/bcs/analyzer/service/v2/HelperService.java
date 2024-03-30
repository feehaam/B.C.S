package com.bcs.analyzer.service.v2;

import com.bcs.analyzer.model.PendingAnalyzer;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.repository.PARepository;
import com.bcs.analyzer.util.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class HelperService {
    private static final Integer ANALYZER_OPERATION_TYPE = 1;

    private final PARepository paRepository;

    protected void updatePendingAnalyzer(Integer targetId) {
        paRepository.save(new PendingAnalyzer(0, targetId, ANALYZER_OPERATION_TYPE, 0));
    }

    protected void updatePendingAnalyzerBatch(List<Integer> targetIds) {
        List<PendingAnalyzer> pendingAnalyzers = new ArrayList<>();
        targetIds.forEach(tid -> pendingAnalyzers.add(new PendingAnalyzer(0, tid, ANALYZER_OPERATION_TYPE, 0)));
        paRepository.saveAll(pendingAnalyzers);
    }

    protected void updateTags(String subject, List<Tag> tags){
        Cache.setRecentTags(tags);
        Cache.addSubjectBasedTag(subject, tags);
    }

    protected void removePendingAnalyzer(int targetId) {
        List<PendingAnalyzer> pendingAnalyzers = paRepository
                .findByTargetIdAndTargetType(targetId, ANALYZER_OPERATION_TYPE).stream().toList();
        if(!pendingAnalyzers.isEmpty())
            paRepository.deleteAll(pendingAnalyzers);
    }

    protected void addPendingAnalyzer(int targetId) {
        PendingAnalyzer pendingAnalyzer = PendingAnalyzer
                .builder()
                .id(0)
                .targetId(targetId)
                .targetType(ANALYZER_OPERATION_TYPE)
                .priority(1)
                .build();
        paRepository.save(pendingAnalyzer);
    }

    public List<PendingAnalyzer> getPendingMCQToAnalyze() {
        List<PendingAnalyzer> results = paRepository.findByPriority(1);
        List<PendingAnalyzer> byTargetType = paRepository.findByTargetType(ANALYZER_OPERATION_TYPE, Pageable.ofSize(10)).stream().toList();
        results.addAll(byTargetType);
        return results;
    }
}
