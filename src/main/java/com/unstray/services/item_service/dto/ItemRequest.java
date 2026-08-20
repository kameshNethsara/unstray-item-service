package com.unstray.services.item_service.dto;

import com.unstray.services.item_service.enums.ItemType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequest {

    private String title;
    private String description;
    private String category;
    private ItemType type;
    private String location;
    private String date;
    private Long reportedBy;
    private String imageUrl;
}