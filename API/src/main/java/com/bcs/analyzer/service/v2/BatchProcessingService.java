package com.bcs.analyzer.service.v2;

import com.bcs.analyzer.model.Ban;
import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.model.UnifiedDTO;
import com.bcs.analyzer.repository.BanRepository;
import com.bcs.analyzer.repository.MCQRepository;
import com.bcs.analyzer.repository.PARepository;
import com.bcs.analyzer.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.bcs.analyzer.util.Cache.*;

@Service
public class BatchProcessingService extends HelperService {
    private final MCQRepository mcqRepository;
    private final TagRepository tagRepository;
    private final BanRepository banRepository;

    public BatchProcessingService(MCQRepository mcqRepository, PARepository paRepository,
                                  TagRepository tagRepository, BanRepository banRepository) {
        super(paRepository);
        this.mcqRepository = mcqRepository;
        this.tagRepository = tagRepository;
        this.banRepository = banRepository;
    }

    public List<MCQ> createBatchMCQ(List<UnifiedDTO> mcqdtoList){
        List<MCQ> mcqList = new ArrayList<>();
        for(UnifiedDTO unifiedDTO: mcqdtoList){
            MCQ mcq = MCQ.builder()
                    .question(unifiedDTO.getQuestion())
                    .optionA(unifiedDTO.getOptionA()).optionB(unifiedDTO.getOptionB())
                    .optionC(unifiedDTO.getOptionC()).optionD(unifiedDTO.getOptionD())
                    .answer(unifiedDTO.getAnswer()).year(unifiedDTO.getYear()).explanation(unifiedDTO.getExplanation())
                    .updateTime(LocalDateTime.now())
                    .subject(unifiedDTO.getSubject()).similarity(0).build();
            mcqList.add(mcq);
        }
        List<MCQ> results = mcqRepository.saveAll(mcqList);

        List<Integer> idList = new ArrayList<>();
        results.forEach(mcq -> idList.add(mcq.getId()));
        updatePendingAnalyzerBatch(idList);

        return results;
    }

    public Map<String, Object> createAndGetTagAndBanBatch(UnifiedDTO unifiedDTO) {

        List<Tag> newTags = new ArrayList<>();
        List<Ban> newBans = new ArrayList<>();

        unifiedDTO.getTagWords().forEach(tagWord -> {
            if(!allTagsString.contains(tagWord.toLowerCase())){
                // Cache the tag word
                newTags.add(new Tag(0, tagWord, new ArrayList<>()));
                allTagsString.add(tagWord.toLowerCase());
            }
        });
        unifiedDTO.getBans().forEach(banWord -> {
            if(!allBansAsString.contains(banWord.toLowerCase())){
                // Cache the ban word
                newBans.add(new Ban(0, banWord.toLowerCase()));
                allBansAsString.add(banWord);
            }
        });

        List<Tag> tags = tagRepository.saveAll(newTags);
        List<Ban> bans = banRepository.saveAll(newBans);
        // Cache the ban and the tag
        allBans.addAll(bans);
        allTags.addAll(tags);

        return Map.of("tags", allTagsString, "bans", allBansAsString);
    }
}
