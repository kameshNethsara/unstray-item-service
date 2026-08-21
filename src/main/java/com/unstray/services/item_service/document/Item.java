package com.unstray.services.item_service.document;

import com.unstray.services.item_service.enums.ItemStatus;
import com.unstray.services.item_service.enums.ItemType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "items")
public class Item {

    @Id
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

    @Builder.Default
    private List<Claim> claims = new ArrayList<>();
}