package com.bcs.analyzer.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cache {
    public static Set<String> allBans = new HashSet<>();
    public static Set<String> allTags = new HashSet<>();
    public static ArrayList<String> recentTags = new ArrayList<>();

    public static void setAllBans(List<String> bans){
        allBans = new HashSet<>();
        allBans.addAll(bans);
    }

    public static void setAllTags(List<String> tags){
        allTags = new HashSet<>();
        allTags.addAll(tags);
    }

    public static void setRecentTags(List<String> tags){
        tags.forEach(tag -> recentTags.addFirst(tag));
        if(recentTags.size() > 20) recentTags.removeLast();
    }
}
