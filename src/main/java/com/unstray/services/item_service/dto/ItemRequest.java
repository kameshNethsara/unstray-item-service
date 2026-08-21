package com.unstray.services.item_service.dto;

import com.unstray.services.item_service.enums.ItemType;
import lombok.*;

import java.time.LocalDateTime;

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
    private String imageUrl;
    private Long reportedBy;
}