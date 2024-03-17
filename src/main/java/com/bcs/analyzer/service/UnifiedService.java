package com.bcs.analyzer.service;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.model.UnifiedDTO;
import com.bcs.analyzer.repository.MCQRepository;
import com.bcs.analyzer.repository.PARepository;
import com.bcs.analyzer.repository.TagRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UnifiedService extends UnifiedServiceHelper{

    private final MCQRepository mcqRepository;
    private final TagService tagService;
    private final BanService banService;
    private final TagRepository tagRepository;

    public UnifiedService(MCQRepository mcqRepository, PARepository paRepository,
                          TagService tagService, BanService banService, TagRepository tagRepository) {
        super(paRepository);
        this.mcqRepository = mcqRepository;
        this.tagService = tagService;
        this.banService = banService;
        this.tagRepository = tagRepository;
    }

    public List<MCQ> createBatchMCQ(List<UnifiedDTO> mcqdtoList){
        List<MCQ> mcqList = new ArrayList<>();
        for(UnifiedDTO unifiedDTO: mcqdtoList){
            MCQ mcq = MCQ.builder()
                    .question(unifiedDTO.getQuestion())
                    .optionA(unifiedDTO.getOptionA())
                    .optionB(unifiedDTO.getOptionB())
                    .optionC(unifiedDTO.getOptionC())
                    .optionD(unifiedDTO.getOptionD())
                    .answer(unifiedDTO.getAnswer())
                    .year(unifiedDTO.getYear())
                    .explanation(unifiedDTO.getExplanation())
                    .subject(unifiedDTO.getSubject())
                    .similarity(0)
                    .build();
            mcqList.add(mcq);
        }
        List<MCQ> results = mcqRepository.saveAll(mcqList);

        List<Integer> idList = new ArrayList<>();
        results.forEach(mcq -> idList.add(mcq.getId()));
        updatePendingAnalyzerBatch(idList);

        return results;
    }

    public Map<String, Object> getByFilter(Integer pageNo, Integer pageSize, Integer year,
                                           String subject, String search, String... tags) {
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
        List<Tag> tags = tagService.createBatch(mcqdto.getTagWords());
        banService.createBatch(mcqdto.getBans());
        mcq.setTags(tags);
        var result = mcqRepository.save(mcq);
        updateTags(mcq.getSubject(), tags);
        return result;
    }
}
