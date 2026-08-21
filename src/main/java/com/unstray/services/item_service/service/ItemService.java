package com.unstray.services.item_service.service;



import com.unstray.services.item_service.dto.ClaimRequest;
import com.unstray.services.item_service.dto.ItemRequest;
import com.unstray.services.item_service.dto.ItemResponse;
import com.unstray.services.item_service.enums.ClaimStatus;
import com.unstray.services.item_service.enums.ItemStatus;
import com.unstray.services.item_service.enums.ItemType;

import java.util.List;

public interface ItemService {
    ItemResponse createItem(ItemRequest request);
    List<ItemResponse> getAllItems();
    ItemResponse getItemById(String id);
    ItemResponse updateItem(String id, ItemRequest request);
    void deleteItem(String id);

    List<ItemResponse> getItemsByType(ItemType type);
    List<ItemResponse> getItemsByStatus(ItemStatus status);
    List<ItemResponse> getItemsByCategory(String category);
    List<ItemResponse> getItemsByLocation(String location);
    List<ItemResponse> getItemsByTypeAndStatus(ItemType type, ItemStatus status);
    List<ItemResponse> getItemsByUser(Long userId);

    ItemResponse updateItemStatus(String id, ItemStatus status);
    ItemResponse submitClaim(String id, ClaimRequest request);
    ItemResponse resolveClaim(String id, String claimId, ClaimStatus status);
}