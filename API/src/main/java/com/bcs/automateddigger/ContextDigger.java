package com.bcs.automateddigger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContextDigger {
    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
        ExecutorService executorService = Executors.newFixedThreadPool(8); // Adjust thread pool size as needed

        for (int i = 45; i > 9; i--) {
            final int iteration = i; // Capture the value of i for each thread
            executorService.execute(() -> {
                try {
                    String si = getSi(iteration);
                    List<Object> results = PageDigger.dig(si, iteration + 1978);
                    if (Errors.hasAny(results)) {
                        List<Object> errors = Errors.listAll(results);
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + si + " has " + errors.size() + " fails!");
                    } else {
                        System.out.println("------------------------------------------------------- " + si + " has been fully extracted:) " + results.size());
                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = objectMapper.writeValueAsString(Errors.listSuccess(results));
                        FFiles.create("F://bcs/" + (iteration + 1978) + ".json");
                        FFiles.changeData("F://bcs/" + (iteration + 1978) + ".json", json);
                    }
                } catch (Exception e) {
                    // Handle any exceptions that occur within the threads
                    System.err.println("Error in thread for " + iteration + ": " + e.getMessage());
                }
            });
        }

        executorService.shutdown();
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