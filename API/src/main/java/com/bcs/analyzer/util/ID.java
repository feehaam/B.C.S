package com.bcs.analyzer.util;

import org.springframework.stereotype.Component;

@Component
public class ID {
    public int lastMCQId = 1;
    public int lastTagId = 1;
    public int lastBanId = 1;
    public int lastPendingAnalyzerId = 1;
}
