package com.unstray.services.item_service.dto;

import com.unstray.services.item_service.enums.ItemType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    private String title;
    private String description;
    private String category;
    private ItemType type;
    private String location;
    private LocalDateTime date;
    private List<String> imageUrls;
    private Long reportedBy;

    private String ownerName;
    private String ownerEmail;
    private String ownerPhone;
}