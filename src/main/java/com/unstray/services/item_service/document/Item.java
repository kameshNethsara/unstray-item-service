package com.unstray.services.item_service.document;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import com.unstray.services.item_service.enums.ItemStatus;
import com.unstray.services.item_service.enums.ItemType;
import lombok.*;

@IgnoreExtraProperties
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @DocumentId
    private String id;

    private String title;

    private String description;

    private String category;

    private ItemType type;

    private String location;

    private String date; // Stored as ISO String (e.g. "2026-08-16")

    private ItemStatus status;

    private Long reportedBy;

    private String createdAt; // Stored as ISO DateTime String (e.g. "2026-08-16T10:15:30")

    private String updatedAt; // Stored as ISO DateTime String

    private String imageUrl;
}