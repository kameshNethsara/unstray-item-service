package com.unstray.services.item_service.service;

import com.unstray.services.item_service.dto.ItemRequest;
import com.unstray.services.item_service.dto.ItemResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ItemService {

    ItemResponse createItem(ItemRequest request) throws ExecutionException, InterruptedException;

    ItemResponse getItemById(String id) throws ExecutionException, InterruptedException;

    List<ItemResponse> getAllItems() throws ExecutionException, InterruptedException;

    List<ItemResponse> getItemsByType(String type) throws ExecutionException, InterruptedException;

    List<ItemResponse> getItemsByCategory(String category) throws ExecutionException, InterruptedException;

    ItemResponse updateItem(String id, ItemRequest request) throws ExecutionException, InterruptedException;

    ItemResponse markAsClaimed(String id) throws ExecutionException, InterruptedException;

    void deleteItem(String id) throws ExecutionException, InterruptedException;
}