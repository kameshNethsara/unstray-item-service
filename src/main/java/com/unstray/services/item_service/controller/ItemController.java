package com.unstray.services.item_service.controller;

import com.unstray.services.item_service.dto.ClaimRequest;
import com.unstray.services.item_service.dto.ItemRequest;
import com.unstray.services.item_service.dto.ItemResponse;
import com.unstray.services.item_service.enums.ClaimStatus;
import com.unstray.services.item_service.enums.ItemStatus;
import com.unstray.services.item_service.enums.ItemType;
import com.unstray.services.item_service.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createItem(request));
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getItems(
            @RequestParam(required = false) ItemType type,
            @RequestParam(required = false) ItemStatus status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location
    ) {
        List<ItemResponse> items;

        if (type != null && status != null) {
            items = itemService.getItemsByTypeAndStatus(type, status);
        } else if (type != null) {
            items = itemService.getItemsByType(type);
        } else if (status != null) {
            items = itemService.getItemsByStatus(status);
        } else if (category != null) {
            items = itemService.getItemsByCategory(category);
        } else if (location != null) {
            items = itemService.getItemsByLocation(location);
        } else {
            items = itemService.getAllItems();
        }

        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable String id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable String id,
            @RequestBody ItemRequest request
    ) {
        return ResponseEntity.ok(itemService.updateItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ItemResponse> updateItemStatus(
            @PathVariable String id,
            @RequestParam ItemStatus status
    ) {
        return ResponseEntity.ok(itemService.updateItemStatus(id, status));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ItemResponse>> getItemsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(itemService.getItemsByUser(userId));
    }

    // --- CLAIM ENDPOINTS ---

    @PostMapping("/{id}/claims")
    public ResponseEntity<ItemResponse> submitClaim(
            @PathVariable String id,
            @RequestBody ClaimRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.submitClaim(id, request));
    }

    @PatchMapping("/{id}/claims/{claimId}/status")
    public ResponseEntity<ItemResponse> resolveClaim(
            @PathVariable String id,
            @PathVariable String claimId,
            @RequestParam ClaimStatus status
    ) {
        return ResponseEntity.ok(itemService.resolveClaim(id, claimId, status));
    }
}