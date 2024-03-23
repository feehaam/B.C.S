package com.bcs.analyzer.service.v2;

import com.bcs.analyzer.model.PendingAnalyzer;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.repository.PARepository;
import com.bcs.analyzer.util.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor
public class HelperService {
    private static final Integer ANALYZER_OPERATION_TYPE = 1;

    private final PARepository paRepository;

    protected void updatePendingAnalyzer(Integer targetId) {
        paRepository.save(new PendingAnalyzer(0, targetId, ANALYZER_OPERATION_TYPE));
    }

    protected void updatePendingAnalyzerBatch(List<Integer> targetIds) {
        List<PendingAnalyzer> pendingAnalyzers = new ArrayList<>();
        targetIds.forEach(tid -> pendingAnalyzers.add(new PendingAnalyzer(0, tid, ANALYZER_OPERATION_TYPE)));
        paRepository.saveAll(pendingAnalyzers);
    }

    protected void updateTags(String subject, List<Tag> tags){
        Cache.setRecentTags(tags);
        Cache.addSubjectBasedTag(subject, tags);
    }
}
