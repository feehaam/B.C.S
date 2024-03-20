package com.bcs.automateddigger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ContextDigger {
    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
        for(int i=45; i>9; i--){
            String si = getSi(i);
            List<Object> results = PageDigger.dig(si, i + 1978);
            if(Errors.hasAny(results)){
                List<Object> errors = Errors.listAll(results);
                System.out.println(si + " has " + errors.size() + " fails!!!!!!!!!!!!!!");
            }
            else{
                System.out.println(si + " has been fully extracted---------------------------- :) " + results.size());
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(Errors.listSuccess(results));
                FFiles.create("F://bcs/" + (i + 1978) + ".json");
                FFiles.changeData("F://bcs/" + (i + 1978) + ".json", json);
            }
        }
    }

    private static String getSi(int n) {
        int i = n % 10;
        String si = "th";
        switch (i) {
            case 1 -> si = "st";
            case 2 -> si = "nd";
            case 3 -> si = "rd";
        }
        return n + si;
    }
}