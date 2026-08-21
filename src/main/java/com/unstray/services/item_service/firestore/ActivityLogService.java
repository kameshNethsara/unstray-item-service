package com.unstray.services.item_service.firestore;

import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final Firestore firestore;

    @Async
    public void logActivity(String action, String itemId, Long userId) {
        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("action", action);
            logData.put("itemId", itemId);
            logData.put("userId", userId);
            logData.put("timestamp", LocalDateTime.now().toString());

            firestore.collection("activity_logs").add(logData);
            log.info("Activity logged successfully to Firestore: {}", action);
        } catch (Exception e) {
            log.error("Failed to write log to Firestore: {}", e.getMessage());
        }
    }
}