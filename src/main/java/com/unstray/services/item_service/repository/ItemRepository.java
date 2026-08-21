package com.unstray.services.item_service.repository;


import com.unstray.services.item_service.document.Item;
import com.unstray.services.item_service.enums.ItemStatus;
import com.unstray.services.item_service.enums.ItemType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByType(ItemType type);
    List<Item> findByStatus(ItemStatus status);
    List<Item> findByCategoryIgnoreCase(String category);
    List<Item> findByLocationIgnoreCase(String location);
    List<Item> findByTypeAndStatus(ItemType type, ItemStatus status);
    List<Item> findByReportedBy(Long reportedBy);
}