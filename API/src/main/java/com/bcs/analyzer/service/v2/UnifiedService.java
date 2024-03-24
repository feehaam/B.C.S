package com.bcs.analyzer.service.v2;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.model.UnifiedDTO;
import com.bcs.analyzer.repository.MCQRepository;
import com.bcs.analyzer.repository.PARepository;
import com.bcs.analyzer.repository.TagRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.bcs.analyzer.util.Cache.*;

@Service
public class UnifiedService extends HelperService {

    private final MCQRepository mcqRepository;
    private final TagRepository tagRepository;
    private final BatchProcessingService bpService;

    public UnifiedService(MCQRepository mcqRepository, PARepository paRepository,
                          TagRepository tagRepository, BatchProcessingService bpService) {
        super(paRepository);
        this.mcqRepository = mcqRepository;
        this.tagRepository = tagRepository;
        this.bpService = bpService;
    }

    public Object getByFilter(Integer mcqId, Integer pageNo, Integer pageSize, Integer year,
                                           String subject, String search, String... tags) {
        if(mcqId != null){
            return mcqRepository.findById(mcqId);
        }
        List<MCQ> results;
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
        boolean allParamsNull = allFiltersNull && pageNo == null && pageSize == null;

        if(allParamsNull){
            results = mcqRepository.findAll();
        }
        else if (allFiltersNull) {
            results = mcqRepository.findAll(PageRequest.of(pageNo, pageSize)).getContent();
        } else {
            results = mcqRepository.findAllByFiltersV2(year, subject, search, tagsList.isEmpty() ? null : tagsList);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("pageNo", pageNo);
        response.put("pageSize", pageSize);
        response.put("year", year);
        response.put("subject", subject);
        response.put("search", search);
        response.put("tags", tagsList);
        response.put("resultsCount", results.size());
        response.put("results", results);
        return response;
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
        MCQ result = mcqRepository.save(mcq);
        removePendingAnalyzer(result.getId());
        return mcq;
    }
}
