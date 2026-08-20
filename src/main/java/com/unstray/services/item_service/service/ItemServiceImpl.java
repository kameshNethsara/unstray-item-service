package com.unstray.services.item_service.service;

import com.unstray.services.item_service.document.Item;
import com.unstray.services.item_service.dto.ItemRequest;
import com.unstray.services.item_service.dto.ItemResponse;
import com.unstray.services.item_service.enums.ItemStatus;
import com.unstray.services.item_service.firestore.ActivityLogService;
import com.unstray.services.item_service.repository.ItemRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ActivityLogService activityLogService;

    public ItemServiceImpl(ItemRepository itemRepository, ActivityLogService activityLogService) {
        this.itemRepository = itemRepository;
        this.activityLogService = activityLogService;
    }

    @Override
    public ItemResponse createItem(ItemRequest request) throws ExecutionException, InterruptedException {
        String currentTime = LocalDateTime.now().toString();

        Item item = Item.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .type(request.getType())
                .location(request.getLocation())
                .date(request.getDate())
                .status(ItemStatus.OPEN)
                .reportedBy(request.getReportedBy())
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .imageUrl(request.getImageUrl())
                .build();

        Item savedItem = itemRepository.save(item);

        activityLogService.logActivity(
                String.valueOf(request.getReportedBy()),
                "CREATE_ITEM",
                savedItem.getId()
        );

        return mapToResponse(savedItem);
    }

    @Override
    public ItemResponse getItemById(String id) throws ExecutionException, InterruptedException {
        Item item = itemRepository.findById(id);
        if (item == null) {
            throw new RuntimeException("Item not found with id: " + id);
        }
        return mapToResponse(item);
    }

    @Override
    public List<ItemResponse> getAllItems() throws ExecutionException, InterruptedException {
        return itemRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponse> getItemsByType(String type) throws ExecutionException, InterruptedException {
        return itemRepository.findByType(type).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponse> getItemsByCategory(String category) throws ExecutionException, InterruptedException {
        return itemRepository.findByCategory(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponse updateItem(String id, ItemRequest request) throws ExecutionException, InterruptedException {
        Item existingItem = itemRepository.findById(id);
        if (existingItem == null) {
            throw new RuntimeException("Item not found with id: " + id);
        }

        existingItem.setTitle(request.getTitle());
        existingItem.setDescription(request.getDescription());
        existingItem.setCategory(request.getCategory());
        existingItem.setType(request.getType());
        existingItem.setLocation(request.getLocation());
        existingItem.setDate(request.getDate());
        existingItem.setImageUrl(request.getImageUrl());
        existingItem.setUpdatedAt(LocalDateTime.now().toString());

        Item updatedItem = itemRepository.save(existingItem);

        activityLogService.logActivity(
                String.valueOf(request.getReportedBy()),
                "UPDATE_ITEM",
                updatedItem.getId()
        );

        return mapToResponse(updatedItem);
    }

    @Override
    public ItemResponse markAsClaimed(String id) throws ExecutionException, InterruptedException {
        Item existingItem = itemRepository.findById(id);
        if (existingItem == null) {
            throw new RuntimeException("Item not found with id: " + id);
        }

        existingItem.setStatus(ItemStatus.CLAIMED);
        existingItem.setUpdatedAt(LocalDateTime.now().toString());

        Item updatedItem = itemRepository.save(existingItem);

        activityLogService.logActivity(
                String.valueOf(existingItem.getReportedBy()),
                "CLAIM_ITEM",
                updatedItem.getId()
        );

        return mapToResponse(updatedItem);
    }

    @Override
    public void deleteItem(String id) throws ExecutionException, InterruptedException {
        Item existingItem = itemRepository.findById(id);
        if (existingItem != null) {
            itemRepository.deleteById(id);
            activityLogService.logActivity(
                    String.valueOf(existingItem.getReportedBy()),
                    "DELETE_ITEM",
                    id
            );
        }
    }

    private ItemResponse mapToResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .type(item.getType())
                .location(item.getLocation())
                .date(item.getDate())
                .status(item.getStatus())
                .reportedBy(item.getReportedBy())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .imageUrl(item.getImageUrl())
                .build();
    }
}