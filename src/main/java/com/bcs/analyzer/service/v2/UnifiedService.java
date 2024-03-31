package com.bcs.analyzer.service.v2;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.model.UnifiedDTO;
import com.bcs.analyzer.repository.MCQRepository;
import com.bcs.analyzer.repository.PARepository;
import com.bcs.analyzer.util.ID;
import com.bcs.analyzer.util.MCQCache;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.bcs.analyzer.util.TagsAndBanCache.*;

@Service
public class UnifiedService extends HelperService {

    private final MCQRepository mcqRepository;
    private final BatchProcessingService bpService;
    private final MCQFilter mcqFilter;
    private final ID id;

    public UnifiedService(MCQRepository mcqRepository, PARepository paRepository, BatchProcessingService bpService, MCQFilter mcqFilter, ID id) {
        super(paRepository, id);
        this.mcqRepository = mcqRepository;
        this.bpService = bpService;
        this.mcqFilter = mcqFilter;
        this.id = id;
    }

    public Map<String, Object> getFilters(){
        Map<String, Object> filter = new HashMap<>();
        filter.put("tags", getSorted(allTagsString));
        filter.put("subjects", subjects);
        List<Integer> years = new ArrayList<>();
        for(int i=45; i>=10; i--) years.add(i);
        filter.put("years", years);
        filter.put("sortBy", MCQFilter.sortBy);
        return filter;
    }

    private String [] getSorted(Collection<String> collection){
        String [] elements = collection.toArray(new String[0]);
        Arrays.sort(elements);
        return elements;
    }

    public Object getByFilter(Integer mcqId, Integer pageNo, Integer pageSize, Integer year,
                              String subject, String search, String sortBy, String... tags) {
        return mcqFilter.getByFilter(mcqId, pageNo, pageSize, year, subject, search, sortBy, tags);
    }

    public MCQ setBanAndTags(Integer id, UnifiedDTO mcqdto){
        Optional<MCQ> mcqOp = mcqRepository.findById(id);
        if(mcqOp.isEmpty()) return null;
        MCQ mcq = mcqOp.get();
        bpService.createAndGetTagAndBanBatch(mcqdto);
        List<Tag> tags = getAllTagsByWords(mcqdto.getTagWords());
        setRecentTags(tags);
        addSubjectBasedTag(mcq.getSubject(), tags);
        mcq.setTags(tags);
        mcq.setUpdateTime(LocalDateTime.now());
        MCQ result = mcqRepository.save(mcq);
        removePendingAnalyzer(result.getId());
        return mcq;
    }

    public MCQ removeTags(Integer id){
        Optional<MCQ> mcqOp = mcqRepository.findById(id);
        if(mcqOp.isEmpty()) return null;
        MCQ mcq = mcqOp.get();
        mcq.getTags().clear();
        addPendingAnalyzer(mcq.getId());
        return mcqRepository.save(mcq);
    }
}
