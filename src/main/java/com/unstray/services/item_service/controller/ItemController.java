package com.unstray.services.item_service.controller;

import com.unstray.services.item_service.dto.ItemRequest;
import com.unstray.services.item_service.dto.ItemResponse;
import com.unstray.services.item_service.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest request)
            throws ExecutionException, InterruptedException {
        ItemResponse createdItem = itemService.createItem(request);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable String id)
            throws ExecutionException, InterruptedException {
        ItemResponse item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems()
            throws ExecutionException, InterruptedException {
        List<ItemResponse> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ItemResponse>> getItemsByType(@PathVariable String type)
            throws ExecutionException, InterruptedException {
        List<ItemResponse> items = itemService.getItemsByType(type);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ItemResponse>> getItemsByCategory(@PathVariable String category)
            throws ExecutionException, InterruptedException {
        List<ItemResponse> items = itemService.getItemsByCategory(category);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable String id,
            @RequestBody ItemRequest request)
            throws ExecutionException, InterruptedException {
        ItemResponse updatedItem = itemService.updateItem(id, request);
        return ResponseEntity.ok(updatedItem);
    }

    @PatchMapping("/{id}/claim")
    public ResponseEntity<ItemResponse> markAsClaimed(@PathVariable String id)
            throws ExecutionException, InterruptedException {
        ItemResponse updatedItem = itemService.markAsClaimed(id);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id)
            throws ExecutionException, InterruptedException {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}