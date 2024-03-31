package com.bcs.analyzer.util;

import com.bcs.analyzer.model.MCQ;
import com.bcs.analyzer.repository.MCQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MCQCache {
    private List<MCQ> mcqList = new CopyOnWriteArrayList<>();
    private Timer timer = new Timer();
    private final Long EXPIRE_IN_MINUTES = 30L;

    @Autowired
    private MCQRepository mcqRepository;

    public List<MCQ> getAll() {
        if (mcqList.isEmpty()) {
            refreshCache();
        }
        return mcqList;
    }

    private void refreshCache() {
        System.out.println("++ Parsing from database @" + LocalTime.now());
        mcqList = mcqRepository.findAll();
        System.out.println(">> Parsed successfully @" + LocalTime.now());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mcqList.clear();
                System.out.println("-- MCQ Cache expired @" + LocalTime.now());
            }
        }, EXPIRE_IN_MINUTES * 1000 * 60);
    }
}
