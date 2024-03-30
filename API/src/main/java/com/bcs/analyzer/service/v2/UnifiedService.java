package com.bcs.analyzer.service.v2;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.model.UnifiedDTO;
import com.bcs.analyzer.repository.MCQRepository;
import com.bcs.analyzer.repository.PARepository;
import com.bcs.analyzer.util.ID;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.bcs.analyzer.util.Cache.*;

@Service
public class UnifiedService extends HelperService {

    private final MCQRepository mcqRepository;
    private final BatchProcessingService bpService;
    private final ID id;
    private final List<String> sortBy = List.of("Similarity Index", "Year", "Update Time", "Alphabetically");

    public UnifiedService(MCQRepository mcqRepository, PARepository paRepository, BatchProcessingService bpService, ID id) {
        super(paRepository, id);
        this.mcqRepository = mcqRepository;
        this.bpService = bpService;
        this.id = id;
    }

    public Object getByFilter(Integer mcqId, Integer pageNo, Integer pageSize, Integer year,
                                           String subject, String search, String sortBy, String... tags) {
        if(mcqId != null){
            return mcqRepository.findById(mcqId);
        }
        List<MCQ> results;
        int pageNumber = pageNo != null ? pageNo - 1 : 0;
        List<String> tagsList = tags == null ? new ArrayList<>() : Arrays.stream(tags).toList();
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
        response.put("sortBy", sortBy);
        response.put("resultsCount", results.size());
        response.put("results", sort(sortBy, results));
        return response;
    }

    private List<MCQ> sort(String sortBy, List<MCQ> results){
        if (sortBy == null) {
            return results;
        }
        else if (sortBy.equals(this.sortBy.get(0))){
            results.sort(Comparator.comparing(MCQ::getSimilarity).reversed());
        }
        else if (sortBy.equals(this.sortBy.get(1))){
            results.sort(Comparator.comparing(MCQ::getYear).reversed());
        }
        else if (sortBy.equals(this.sortBy.get(2))){
            results.sort(Comparator.comparing(MCQ::getUpdateTime).reversed());
            List<MCQ> chunked = new ArrayList<>();
            for(MCQ mcq: results){
                if(mcq.getUpdateTime().isAfter(LocalDateTime.now().minusDays(4))){
                    chunked.add(mcq);
                }
                else break;
            }
            results = chunked;
        }
        else if (sortBy.equals(this.sortBy.get(3))){
            results.sort(Comparator.comparing(MCQ::getQuestion));
        }
        return results;
    }

    public Map<String, Object> getFilters(){
        Map<String, Object> filter = new HashMap<>();
        filter.put("tags", getSorted(allTagsString));
        filter.put("subjects", getSorted(subjectTopTags.keySet()));
        List<Integer> years = new ArrayList<>();
        for(int i=45; i>=10; i--) years.add(i);
        filter.put("years", years);
        filter.put("sortBy", sortBy);
        return filter;
    }

    private String [] getSorted(Collection<String> collection){
        String [] elements = collection.toArray(new String[0]);
        Arrays.sort(elements);
        return elements;
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
