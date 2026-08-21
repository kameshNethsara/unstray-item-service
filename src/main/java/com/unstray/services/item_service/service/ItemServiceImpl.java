package com.unstray.services.item_service.service;

import com.unstray.services.item_service.document.Claim;
import com.unstray.services.item_service.document.Item;
import com.unstray.services.item_service.dto.ClaimRequest;
import com.unstray.services.item_service.dto.ItemRequest;
import com.unstray.services.item_service.dto.ItemResponse;
import com.unstray.services.item_service.enums.ClaimStatus;
import com.unstray.services.item_service.enums.ItemStatus;
import com.unstray.services.item_service.enums.ItemType;
import com.unstray.services.item_service.firestore.ActivityLogService;
import com.unstray.services.item_service.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ActivityLogService activityLogService;

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
                .imageUrl(request.getImageUrl())
                .reportedBy(request.getReportedBy())
                .claims(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Item savedItem = itemRepository.save(item);
        activityLogService.logActivity("ITEM_CREATED", savedItem.getId(), savedItem.getReportedBy());
        return convertToResponse(savedItem);
    }

    @Override
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    @Override
    public ItemResponse getItemById(String id) {
        return convertToResponse(findItemById(id));
    }

    @Override
    public ItemResponse updateItem(String id, ItemRequest request) {
        Item item = findItemById(id);

        if (request.getTitle() != null) item.setTitle(request.getTitle());
        if (request.getDescription() != null) item.setDescription(request.getDescription());
        if (request.getCategory() != null) item.setCategory(request.getCategory());
        if (request.getType() != null) item.setType(request.getType());
        if (request.getLocation() != null) item.setLocation(request.getLocation());
        if (request.getDate() != null) item.setDate(request.getDate());
        if (request.getImageUrl() != null) item.setImageUrl(request.getImageUrl());

        item.setUpdatedAt(LocalDateTime.now());
        Item updatedItem = itemRepository.save(item);

        activityLogService.logActivity("ITEM_UPDATED", updatedItem.getId(), updatedItem.getReportedBy());
        return convertToResponse(updatedItem);
    }

    @Override
    public void deleteItem(String id) {
        Item item = findItemById(id);
        itemRepository.deleteById(id);
        activityLogService.logActivity("ITEM_DELETED", id, item.getReportedBy());
    }

    @Override
    public List<ItemResponse> getItemsByType(ItemType type) {
        return itemRepository.findByType(type).stream().map(this::convertToResponse).toList();
    }

    @Override
    public List<ItemResponse> getItemsByStatus(ItemStatus status) {
        return itemRepository.findByStatus(status).stream().map(this::convertToResponse).toList();
    }

    @Override
    public List<ItemResponse> getItemsByCategory(String category) {
        return itemRepository.findByCategoryIgnoreCase(category).stream().map(this::convertToResponse).toList();
    }

    @Override
    public List<ItemResponse> getItemsByLocation(String location) {
        return itemRepository.findByLocationIgnoreCase(location).stream().map(this::convertToResponse).toList();
    }

    @Override
    public List<ItemResponse> getItemsByTypeAndStatus(ItemType type, ItemStatus status) {
        return itemRepository.findByTypeAndStatus(type, status).stream().map(this::convertToResponse).toList();
    }

    @Override
    public List<ItemResponse> getItemsByUser(Long userId) {
        return itemRepository.findByReportedBy(userId).stream().map(this::convertToResponse).toList();
    }

    @Override
    public ItemResponse updateItemStatus(String id, ItemStatus status) {
        Item item = findItemById(id);
        item.setStatus(status);
        item.setUpdatedAt(LocalDateTime.now());
        Item updatedItem = itemRepository.save(item);

        activityLogService.logActivity("ITEM_STATUS_UPDATED", updatedItem.getId(), updatedItem.getReportedBy());
        return convertToResponse(updatedItem);
    }

    @Override
    public ItemResponse submitClaim(String id, ClaimRequest request) {
        Item item = findItemById(id);

        Claim newClaim = Claim.builder()
                .claimId(UUID.randomUUID().toString())
                .claimerId(request.getClaimerId())
                .proofDescription(request.getProofDescription())
                .contactPhone(request.getContactPhone())
                .status(ClaimStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        item.getClaims().add(newClaim);
        item.setStatus(ItemStatus.CLAIM_PENDING);
        item.setUpdatedAt(LocalDateTime.now());

        Item updatedItem = itemRepository.save(item);
        activityLogService.logActivity("CLAIM_SUBMITTED", updatedItem.getId(), request.getClaimerId());

        return convertToResponse(updatedItem);
    }

    @Override
    public ItemResponse resolveClaim(String id, String claimId, ClaimStatus status) {
        Item item = findItemById(id);

        Claim targetClaim = item.getClaims().stream()
                .filter(c -> c.getClaimId().equals(claimId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Claim not found"));

        if (status == ClaimStatus.APPROVED) {
            targetClaim.setStatus(ClaimStatus.APPROVED);
            item.setStatus(ItemStatus.RESOLVED);

            item.getClaims().forEach(c -> {
                if (!c.getClaimId().equals(claimId)) {
                    c.setStatus(ClaimStatus.REJECTED);
                }
            });
        } else if (status == ClaimStatus.REJECTED) {
            targetClaim.setStatus(ClaimStatus.REJECTED);
            boolean hasPendingClaims = item.getClaims().stream()
                    .anyMatch(c -> c.getStatus() == ClaimStatus.PENDING);

            if (!hasPendingClaims) {
                item.setStatus(ItemStatus.OPEN);
            }
        }

        item.setUpdatedAt(LocalDateTime.now());
        Item updatedItem = itemRepository.save(item);

        activityLogService.logActivity("CLAIM_" + status.name(), updatedItem.getId(), item.getReportedBy());
        return convertToResponse(updatedItem);
    }

    private Item findItemById(String id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }

    private ItemResponse convertToResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .type(item.getType())
                .status(item.getStatus())
                .location(item.getLocation())
                .date(item.getDate())
                .imageUrl(item.getImageUrl())
                .reportedBy(item.getReportedBy())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .claims(item.getClaims())
                .build();
    }
}