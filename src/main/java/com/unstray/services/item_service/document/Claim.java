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
    private String claimerName;
    private String proofDescription;
    private String contactPhone;
    private String contactEmail;
    private ClaimStatus status;
    private LocalDateTime createdAt;
}