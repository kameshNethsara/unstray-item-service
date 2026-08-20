package com.unstray.services.item_service.firestore;

import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ActivityLogService {

    private final Firestore firestore;

    public ActivityLogService(Firestore firestore) {
        this.firestore = firestore;
    }

    public void logActivity(String userId, String action, String itemId) {
        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("userId", userId);
            logData.put("action", action);
            logData.put("itemId", itemId);
            logData.put("timestamp", LocalDateTime.now().toString());

            firestore.collection("activity_logs").add(logData);
        } catch (Exception e) {
            System.err.println("Failed to write activity log: " + e.getMessage());
        }
    }
}