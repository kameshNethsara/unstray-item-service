package com.unstray.services.item_service.dto;

import com.unstray.services.item_service.document.Claim;
import com.unstray.services.item_service.enums.ItemStatus;
import com.unstray.services.item_service.enums.ItemType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private String id;
    private String title;
    private String description;
    private String category;
    private ItemType type;
    private ItemStatus status;
    private String location;
    private LocalDateTime date;
    private String imageUrl;
    private Long reportedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Claim> claims;
}