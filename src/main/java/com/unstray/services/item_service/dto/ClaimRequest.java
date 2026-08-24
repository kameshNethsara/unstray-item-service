package com.unstray.services.item_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimRequest {
    private Long claimerId;
    private String claimerName;
    private String proofDescription;
    private String contactPhone;
    private String contactEmail;
}
