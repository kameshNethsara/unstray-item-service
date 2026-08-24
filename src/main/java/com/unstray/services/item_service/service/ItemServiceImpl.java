package com.unstray.services.item_service.service;

import com.unstray.services.item_service.document.Claim;
import com.unstray.services.item_service.document.Item;
import com.unstray.services.item_service.dto.ClaimRequest;
import com.unstray.services.item_service.dto.ClaimResponse;
import com.unstray.services.item_service.dto.ItemRequest;
import com.unstray.services.item_service.dto.ItemResponse;
import com.unstray.services.item_service.enums.ClaimStatus;
import com.unstray.services.item_service.enums.ItemStatus;
import com.unstray.services.item_service.enums.ItemType;
//import com.unstray.services.item_service.firestore.ActivityLogService;
import com.unstray.services.item_service.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public ItemResponse createItem(ItemRequest request) {
        Item item = Item.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .type(request.getType())
                .status(ItemStatus.OPEN)
                .location(request.getLocation())
                .date(request.getDate())
                .imageUrls(request.getImageUrls() != null ? request.getImageUrls() : new ArrayList<>())
                .reportedBy(request.getReportedBy())
                // Saving owner details from the request directly to the document
                .ownerName(request.getOwnerName())
                .ownerEmail(request.getOwnerEmail())
                .ownerPhone(request.getOwnerPhone())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .claims(new ArrayList<>())
                .build();

        Item savedItem = itemRepository.save(item);
        return mapToResponse(savedItem);
    }

    @Override
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ItemResponse getItemById(String id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        return mapToResponse(item);
    }

    @Override
    public ItemResponse updateItem(String id, ItemRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        if (request.getTitle() != null) item.setTitle(request.getTitle());
        if (request.getDescription() != null) item.setDescription(request.getDescription());
        if (request.getCategory() != null) item.setCategory(request.getCategory());
        if (request.getType() != null) item.setType(request.getType());
        if (request.getLocation() != null) item.setLocation(request.getLocation());
        if (request.getDate() != null) item.setDate(request.getDate());
        if (request.getImageUrls() != null) item.setImageUrls(request.getImageUrls());

        item.setUpdatedAt(LocalDateTime.now());

        Item updatedItem = itemRepository.save(item);
        return mapToResponse(updatedItem);
    }

    @Override
    public void deleteItem(String id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item not found with id: " + id);
        }
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemResponse> getItemsByType(ItemType type) {
        return itemRepository.findByType(type).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ItemResponse> getItemsByStatus(ItemStatus status) {
        return itemRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ItemResponse> getItemsByCategory(String category) {
        return itemRepository.findByCategoryIgnoreCase(category).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ItemResponse> getItemsByLocation(String location) {
        return itemRepository.findByLocationIgnoreCase(location).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ItemResponse> getItemsByTypeAndStatus(ItemType type, ItemStatus status) {
        return itemRepository.findByTypeAndStatus(type, status).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ItemResponse> getItemsByUser(Long userId) {
        return itemRepository.findByReportedBy(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ItemResponse updateItemStatus(String id, ItemStatus status) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        item.setStatus(status);
        item.setUpdatedAt(LocalDateTime.now());

        Item updatedItem = itemRepository.save(item);
        return mapToResponse(updatedItem);
    }

    @Override
    public ItemResponse submitClaim(String id, ClaimRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        Claim newClaim = Claim.builder()
                .claimId(UUID.randomUUID().toString())
                .claimerId(request.getClaimerId())
                // Save the claimer's name and email if provided, otherwise fallback to ID
                .claimerName(request.getClaimerName() != null ? request.getClaimerName() : "User #" + request.getClaimerId())
                .contactEmail(request.getContactEmail())
                .proofDescription(request.getProofDescription())
                .contactPhone(request.getContactPhone())
                .status(ClaimStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        if (item.getClaims() == null) {
            item.setClaims(new ArrayList<>());
        }

        item.getClaims().add(newClaim);
        item.setStatus(ItemStatus.CLAIM_PENDING);
        item.setUpdatedAt(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);
        return mapToResponse(savedItem);
    }

    @Override
    public ItemResponse resolveClaim(String id, String claimId, ClaimStatus status) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        if (item.getClaims() != null) {
            for (Claim claim : item.getClaims()) {
                if (claim.getClaimId().equals(claimId)) {
                    claim.setStatus(status);
                    if (status == ClaimStatus.APPROVED) {
                        item.setStatus(ItemStatus.CLAIMED);
                    }
                    break;
                }
            }
        }

        item.setUpdatedAt(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        return mapToResponse(savedItem);
    }

    private ItemResponse mapToResponse(Item item) {
        // Map claims entity list to ClaimResponse DTO list
        List<ClaimResponse> claimResponses = new ArrayList<>();
        if (item.getClaims() != null) {
            claimResponses = item.getClaims().stream().map(claim ->
                    ClaimResponse.builder()
                            .claimId(claim.getClaimId())
                            .claimerId(claim.getClaimerId())
                            .claimerName(claim.getClaimerName() != null ? claim.getClaimerName() : "User #" + claim.getClaimerId())
                            .claimerEmail(claim.getContactEmail())
                            .proofDescription(claim.getProofDescription())
                            .contactPhone(claim.getContactPhone())
                            .status(claim.getStatus())
                            .createdAt(claim.getCreatedAt())
                            .build()
            ).collect(Collectors.toList());
        }

        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .type(item.getType())
                .status(item.getStatus())
                .location(item.getLocation())
                .date(item.getDate())
                .imageUrls(item.getImageUrls())
                .reportedBy(item.getReportedBy())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .claims(claimResponses)
                // Read owner data directly from the document
                .ownerName(item.getOwnerName() != null ? item.getOwnerName() : "User #" + item.getReportedBy())
                .ownerEmail(item.getOwnerEmail())
                .ownerPhone(item.getOwnerPhone())
                .build();
    }
}