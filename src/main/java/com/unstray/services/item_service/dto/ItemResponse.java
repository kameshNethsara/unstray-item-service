package com.unstray.services.item_service.dto;

import com.unstray.services.item_service.enums.ItemStatus;
import com.unstray.services.item_service.enums.ItemType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponse {

    private String id;
    private String title;
    private String description;
    private String category;
    private ItemType type;
    private String location;
    private String date;
    private ItemStatus status;
    private Long reportedBy;
    private String createdAt;
    private String updatedAt;
    private String imageUrl;
}