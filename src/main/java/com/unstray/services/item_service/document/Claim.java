package com.unstray.services.item_service.document;

import com.unstray.services.item_service.enums.ClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Claim {
    private String claimId;
    private Long claimerId;
    private String proofDescription;
    private String contactPhone;
    private ClaimStatus status;
    private LocalDateTime createdAt;
}
