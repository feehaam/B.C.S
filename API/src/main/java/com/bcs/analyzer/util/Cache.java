package com.bcs.analyzer.util;

import com.bcs.analyzer.model.Ban;
import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.model.Tag;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Cache {
    public static Set<Tag> allTags = new HashSet<>();
    public static Set<String> allTagsString = new HashSet<>();
    public static Set<Ban> allBans = new HashSet<>();
    public static Set<String> allBansAsString = new HashSet<>();
    public static ArrayList<Tag> recentTags = new ArrayList<>();
    public static Map<String, Map<String, Integer>> subjectTopTags = new HashMap<>();

    public static void initAllBansAndId(List<Ban> bans){
        allBans.addAll(bans);
        allBansAsString.addAll(bans.stream().map(Ban::getWord).toList());
    }

    public static void setAllTags(List<Tag> tags){
        allTags.addAll(tags);
        allTagsString.addAll(tags.stream().map(Tag::getWord).toList());
    }

    public static List<Tag> getAllTagsByWords(List<String> words){
        Set<String> searched = new HashSet<>(words);
        return new ArrayList<>(allTags.stream().filter(tag -> searched.contains(tag.getWord())).toList());
    }

    public static void setRecentTags(List<Tag> tags){
        updateAllTags(tags);
        tags.forEach(tag -> {
            recentTags.remove(tag);
        });
        tags.forEach(tag -> {
            recentTags.addFirst(tag);
        });
        while (recentTags.size() > 20) recentTags.removeLast();
    }

    private static void updateAllTags(List<Tag> tags){
        tags.forEach(tag -> {
            if (!allTagsString.contains(tag.getWord())){
                allTagsString.add(tag.getWord());
                allTags.add(tag);
            }
        });
    }

    public static void setAllSubjectBasedTags(String subject, Tag tag){
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);
        addSubjectBasedTag(subject, tagList);
    }

    public static void addSubjectBasedTag(String subject, List<Tag> tags){
        subject = subject.toLowerCase();
        if(!subjectTopTags.containsKey(subject)) subjectTopTags.put(subject, new HashMap<>());
        Map<String, Integer> subTags = subjectTopTags.get(subject);
        tags.forEach(tag -> subTags.put(tag.getWord(), subTags.getOrDefault(tag.getWord(), 0) + 1));
    }
}
