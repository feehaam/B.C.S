package com.bcs.analyzer.util;

import com.bcs.analyzer.model.Tag;

import java.util.*;

public class Cache {
    public static Set<String> allBans = new HashSet<>();
    public static Set<Tag> allTags = new HashSet<>();
    public static ArrayList<Tag> recentTags = new ArrayList<>();
    public static Map<String, Map<Tag, Integer>> subjectTopTags = new HashMap<>();

    public static void setAllBans(List<String> bans){
        allBans = new HashSet<>();
        allBans.addAll(bans);
    }

    public static void setAllTags(List<Tag> tags){
        allTags = new HashSet<>();
        allTags.addAll(tags);
    }

    public static void setRecentTags(List<Tag> tags){
        tags.forEach(tag -> recentTags.addFirst(tag));
        while (recentTags.size() > 20) recentTags.removeLast();
    }

    public static void addCategoryBasedTag(String subject, List<Tag> tags){
        subject = subject.toLowerCase();
        if(!subjectTopTags.containsKey(subject)) subjectTopTags.put(subject, new HashMap<>());
        Map<Tag, Integer> subTags = subjectTopTags.get(subject);
        tags.forEach(tag -> subTags.put(tag, subTags.getOrDefault(tag, 0) + 1));
    }
}
