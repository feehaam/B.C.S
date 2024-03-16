package com.bcs.analyzer.service;

import com.bcs.analyzer.model.MCQDTO;
import com.bcs.analyzer.model.Tag;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

import static com.bcs.analyzer.util.Cache.*;

public class TagSuggestion {
    private static final Integer NUMBER_OF_RECENTLY_USED_TAGS_TO_INCLUDE = 5;
    private static final Integer NUMBER_OF_SUBJECT_BASED_TAGS_TO_INCLUDE = 5;
    private static final Integer NUMBER_OF_LOW_PRIORITY_TAGS_TO_INCLUDE = 40;

    public List<Tag> getSuggestedTags(MCQDTO mcqdto){
        // Extract words from the question
        Set<String> words = getWordsFromString(mcqdto.getQuestion());

        // Get all kind of suggestions
        List<Tag> tagsThatDirectlyMatchedWithQuestion = addTagsThatMatchTheQuestion(words);
        List<Tag> mostUsedTagsBySubject = addTagsBasedOnSubject(mcqdto.getSubject()
                .toLowerCase(), NUMBER_OF_SUBJECT_BASED_TAGS_TO_INCLUDE);
        List<Tag> recentlyUsedTags = getRecentlyUsedTags(NUMBER_OF_RECENTLY_USED_TAGS_TO_INCLUDE);

        // Prepare suggestions list
        List<Tag> suggestions = new ArrayList<>();
        tagsThatDirectlyMatchedWithQuestion.forEach(tag -> {
            if(!suggestions.contains(tag)) suggestions.add(tag);
        });
        recentlyUsedTags.forEach(tag -> {
            if(!suggestions.contains(tag)) suggestions.add(tag);
        });
        mostUsedTagsBySubject.forEach(tag -> {
            if(!suggestions.contains(tag)) suggestions.add(tag);
        });

        // Add tags that are not suggested
        suggestions.addAll(lowPrioritySuggestions(suggestions, NUMBER_OF_LOW_PRIORITY_TAGS_TO_INCLUDE));

        // The final suggestions list
        return suggestions;
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
        return ch < 48 || (ch > 57 && ch < 65) || (ch > 90 && ch < 97) || (ch > 122 && ch < 127) ? " " : ch + "";
    }

    private static String removeExtraSpaces(String str){
        while(str.contains("  ")) {
            str = str.replaceAll(" {2}", " ");
        }
        return str;
    }

    private static ArrayList<Tag> addTagsThatMatchTheQuestion(Set<String> words){
        ArrayList<Tag> matchedWithQuestion = new ArrayList<>();
        allTags.forEach(tag -> {
            if(words.contains(tag.getWord().toLowerCase()))
                matchedWithQuestion.add(tag);
        });
        return matchedWithQuestion;
    }

    private static List<Tag> addTagsBasedOnSubject(String subject, int numberOfTags) {
        ArrayList<Pair> selectedTags = new ArrayList<>();
        Map<Tag, Integer> subTopTags = subjectTopTags.getOrDefault(subject, new HashMap<>());
        subTopTags.forEach((key, value) -> {
            if (value >= selectedTags.getLast().frequency) {
                selectedTags.addFirst(new Pair(key, value));
            }
            if (selectedTags.size() > numberOfTags) {
                selectedTags.removeLast();
            }
        });
        return selectedTags.stream().map(pair -> pair.tag).collect(Collectors.toList());
    }

    private static List<Tag> getRecentlyUsedTags(int numberOfTags) {
        List<Tag> mostRecent = new ArrayList<>();
        for(Tag tag: recentTags) {
            if(mostRecent.size() >= numberOfTags) break;
            mostRecent.add(recentTags.getFirst());
        }
        return mostRecent;
    }

    private static Set<Tag> lowPrioritySuggestions(List<Tag> suggestions, int numberOfTags){
        int limit = numberOfTags;
        Set<Tag> suggested = new HashSet<>(suggestions);
        Set<Tag> notSuggested = new HashSet<>();
        for (Tag tag: allTags){
            if(!suggested.contains(tag)){
                notSuggested.add(tag);
                limit--;
            }
            if (limit == 0) break;
        }
        return notSuggested;
    }

    @AllArgsConstructor
    private static class Pair{
        Tag tag;
        Integer frequency;
    }
}
