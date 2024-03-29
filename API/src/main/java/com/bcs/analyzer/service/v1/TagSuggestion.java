package com.bcs.analyzer.service.v1;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.Tag;
import com.bcs.analyzer.repository.MCQRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.bcs.analyzer.util.Cache.*;

@Service @RequiredArgsConstructor
public class TagSuggestion {
    private static final Integer NUMBER_OF_RECENTLY_USED_TAGS_TO_INCLUDE = 5;
    private static final Integer NUMBER_OF_SUBJECT_BASED_TAGS_TO_INCLUDE = 7;
    private static final Integer NUMBER_OF_LOW_PRIORITY_TAGS_TO_INCLUDE = 40;

    private final MCQRepository mcqRepository;

    public Map<String, Object> getSuggestedTagsAndBans(int mcqId){
        Optional<MCQ> mcqOp = mcqRepository.findById(mcqId);
        if(mcqOp.isEmpty()) return null;
        MCQ mcq = mcqOp.get();

        // Get all kind of suggestions and prepare suggestions list
        Map<String, List<Tag>> tags = new HashMap<>();
        addTagsToResult(addTagsThatMatchTheSample(getWordsFromString(mcq.getQuestion() + " " + mcq.getExplanation())), "matched", tags);
        addTagsToResult(getRecentlyUsedTags(), "recent", tags);
        addTagsToResult(getAllTagsByWords(addTagsBasedOnSubject(mcq.getSubject().toLowerCase())), "subject", tags);
        addTagsToResult(addTagsThatDoesNotMatchTheSample(getWordsFromString(mcq.getQuestion() + " " + mcq.getExplanation())), "new", tags);
        addTagsToResult(lowPrioritySuggestions(tags), "more", tags);

        // Prepare bans suggestions
        Set<String> bans = new HashSet<>();
        for(String word: getWordsFromString(mcq.getQuestion()
                + " " + mcq.getExplanation()
                + " " + mcq.getOptionA()
                + " " + mcq.getOptionB()
                + " " + mcq.getOptionC()
                + " " + mcq.getOptionD())){
            if(!(allBansAsString.contains(word) && allTagsString.contains(word))){
                bans.add(word);
            }
        }

        return Map.of("tags", getTagsAsList(tags), "bans", bans);
    }

    private static void addTagsToResult(List<Tag> tags, String key, Map<String, List<Tag>> suggestions){
        suggestions.put(key, suggestions.getOrDefault(key, new ArrayList<>()));
        Set<String> suggested = new HashSet<>();
        suggestions.values().forEach(t -> {
            suggested.addAll(t.stream().map(Tag::getWord).toList());
        });
        tags.forEach(tag -> {
            if(!suggested.contains(tag.getWord())){
                suggestions.get(key).add(tag);
            }
        });
    }

    private static Set<String> getWordsFromString(String str){
        String[] words = removeExtraSpaces(specialCharacterToSpace(str)).split(" ");
        return new HashSet<>(List.of(removeExtraSpaces(specialCharacterToSpace(str)).split(" ")));
    }

    private static String specialCharacterToSpace(String str){
        char[] ar = str.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (char ch: ar){
            stringBuilder.append(replaceIfSpecialCharacter(ch));
        }
        return stringBuilder.toString();
    }

    private static String replaceIfSpecialCharacter(char ch){
        return ch < 48 || (ch > 57 && ch < 65) || (ch > 90 && ch < 97) || (ch > 122 && ch < 127)
                || (String.valueOf(ch).equals("।")) ? " " : ch + "";
    }

    private static String removeExtraSpaces(String str){
        while(str.contains("  ")) {
            str = str.replaceAll(" {2}", " ");
        }
        return str;
    }

    private static ArrayList<Tag> addTagsThatMatchTheSample(Set<String> words){
        ArrayList<Tag> matchedWithQuestion = new ArrayList<>();
        allTags.forEach(tag -> {
            if(words.contains(tag.getWord().toLowerCase()))
                matchedWithQuestion.add(tag);
        });
        return matchedWithQuestion;
    }

    private static List<Tag> addTagsThatDoesNotMatchTheSample(Set<String> words){
        Set<Tag> matchedWithQuestion = new HashSet<>();
        allTagsString.forEach(words::remove);
        allBansAsString.forEach(words::remove);
        return words.stream().map(word -> new Tag(0, word.toLowerCase(), new ArrayList<>())).toList();
    }

    private static List<String> addTagsBasedOnSubject(String subject) {
        ArrayList<Pair> selectedTags = new ArrayList<>();
        Map<String, Integer> subTopTags = subjectTopTags.getOrDefault(subject, new HashMap<>());
        subTopTags.forEach((key, value) -> {
            if (selectedTags.isEmpty() || value >= selectedTags.getLast().frequency) {
                selectedTags.addFirst(new Pair(key, value));
            }
            if (selectedTags.size() > NUMBER_OF_SUBJECT_BASED_TAGS_TO_INCLUDE) {
                selectedTags.removeLast();
            }
        });
        return selectedTags.stream().map(pair -> pair.tagWord).collect(Collectors.toList());
    }

    private static List<Tag> getRecentlyUsedTags() {
        List<Tag> mostRecent = new ArrayList<>();
        for(Tag tag: recentTags) {
            if(mostRecent.size() >= NUMBER_OF_RECENTLY_USED_TAGS_TO_INCLUDE) break;
            mostRecent.add(recentTags.getFirst());
        }
        return mostRecent;
    }

    private static List<Tag> lowPrioritySuggestions(Map<String, List<Tag>> suggestionsMap){
        List<Tag> suggestions = new ArrayList<>();
        suggestionsMap.values().forEach(suggestions::addAll);
        int limit = NUMBER_OF_LOW_PRIORITY_TAGS_TO_INCLUDE;
        Set<Tag> suggested = new HashSet<>(suggestions);
        List<Tag> notSuggested = new ArrayList<>();
        for (Tag tag: allTags){
            if(!suggested.contains(tag)){
                notSuggested.add(tag);
                limit--;
            }
            if (limit == 0) break;
        }
        return notSuggested;
    }

    private List<Tag> getTagsAsList(Map<String, List<Tag>> tags){
        List<Tag> tagsAsList = new ArrayList<>();
        tagsAsList.addAll(tags.get("more"));
        tagsAsList.addAll(tags.get("new"));
        tagsAsList.addAll(tags.get("subject"));
        tagsAsList.addAll(tags.get("recent"));
        tagsAsList.addAll(tags.get("matched"));
        return tagsAsList;
    }

    @AllArgsConstructor
    private static class Pair{
        String tagWord;
        Integer frequency;
    }
}
