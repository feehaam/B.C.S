package com.bcs.analyzer.service.v2;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.repository.MCQRepository;
import com.bcs.analyzer.util.MCQCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.bcs.analyzer.service.v1.TagSuggestion.getWordsFromString;

@Component @RequiredArgsConstructor
public class MCQFilter {

    private final MCQRepository mcqRepository;
    private final MCQCache mcqCache;
    static final List<String> sortBy = List.of("Similarity Index", "Year", "Update Time", "Alphabetically");

    public Object getByFilter(Integer mcqId, Integer pageNo, Integer pageSize, Integer year,
                              String subject, String search, String sortBy, String... tags) {
        if(mcqId != null){
            return mcqRepository.findById(mcqId);
        }
        List<MCQ> results;
        int pageNumber = pageNo != null ? pageNo - 1 : 0;
        int itemPerPage = pageSize != null ? pageSize : 20;
        List<String> tagsList = tags == null ? new ArrayList<>() : Arrays.stream(tags).toList();
        boolean allFiltersNull = year == null && subject == null && search == null && tagsList.isEmpty();
        boolean allParamsNull = allFiltersNull && pageNo == null && pageSize == null;

        results = mcqCache.getAll();
        if (!allParamsNull) {
            if (year != null && year > 0) {
                results = results.stream().filter(mcq -> mcq.getYear() == year).toList();
            }
            if (subject != null) {
                results = results.stream().filter(mcq -> mcq.getSubject().equals(subject)).toList();
            }
            if (search != null) {
                results = results.stream().filter(mcq -> {
                    Set<String> baseWords = getWordsFromString(mcq.getQuestion() + " " + mcq.getExplanation());
                    Set<String> searchedWords = getWordsFromString(search);
                    AtomicBoolean matched = new AtomicBoolean(false);
                    for(String sw: searchedWords){
                       if (baseWords.contains(sw)) {
                           matched.set(true);
                           break;
                       }
                    }
                    return matched.get();
                }).toList();
            }
            if (!tagsList.isEmpty()) {
                Set<String> words = new HashSet<>(tagsList);
                results = results.stream().filter(mcq -> {
                    if(mcq.getTags() == null || mcq.getTags().isEmpty())
                        return false;
                    for(Tag tag: mcq.getTags()){
                        if (words.contains(tag.getWord())) {
                            return true;
                        }
                    }
                    return false;
                }).toList();
            }
        }

        results = sort(sortBy, new ArrayList<>(results));

        if(pageNo != null && pageSize != null){
            int from = Math.min(pageNumber * itemPerPage, results.size() -1);
            from = Math.max(0, from);
            int to = Math.min(results.size() -1, (pageNumber + 1) * itemPerPage);
            to = Math.max(0, to);
            results = results.subList(from, to);
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
        response.put("results", results);
        return response;
    }

    private List<MCQ> sort(String sortBy, List<MCQ> results){
        if (sortBy == null) {
            return results;
        }
        else if (sortBy.equals(MCQFilter.sortBy.get(0))){
            results.sort(Comparator.comparing(MCQ::getSimilarity).reversed());
        }
        else if (sortBy.equals(MCQFilter.sortBy.get(1))){
            results.sort(Comparator.comparing(MCQ::getYear).reversed());
        }
        else if (sortBy.equals(MCQFilter.sortBy.get(2))){
            results.sort(Comparator.comparing(MCQ::getUpdateTime).reversed());
        }
        else if (sortBy.equals(MCQFilter.sortBy.get(3))){
            results.sort(Comparator.comparing(MCQ::getQuestion));
        }
        return results;
    }
}
