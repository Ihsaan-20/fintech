package com.example.fintech.service;

import com.example.fintech.dto.GraphPoint;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@EnableScheduling
public class GraphDataService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Random random = new Random();

    public GraphDataService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 1000) // har 1 second data bhejna
    public void sendGraphData() {
        long now = System.currentTimeMillis();
        GraphPoint point = new GraphPoint(
                now,
                random.nextInt(50),   // upload
                random.nextInt(100)   // download
        );
        messagingTemplate.convertAndSend("/topic/graph", point);
    }
}
