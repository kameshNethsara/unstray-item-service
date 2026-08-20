package com.unstray.services.item_service.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.unstray.services.item_service.document.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class ItemRepository {

    private static final String COLLECTION_NAME = "items";
    private final Firestore firestore;

    public ItemRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public Item save(Item item) throws ExecutionException, InterruptedException {
        DocumentReference docRef;
        if (item.getId() == null) {
            docRef = firestore.collection(COLLECTION_NAME).document();
            item.setId(docRef.getId());
        } else {
            docRef = firestore.collection(COLLECTION_NAME).document(item.getId());
        }
        ApiFuture<WriteResult> result = docRef.set(item);
        result.get();
        return item;
    }

    public Item findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.toObject(Item.class);
        }
        return null;
    }

    public List<Item> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Item> items = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            items.add(doc.toObject(Item.class));
        }
        return items;
    }

    public List<Item> findByType(String type) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("type", type)
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Item> items = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            items.add(doc.toObject(Item.class));
        }
        return items;
    }

    public List<Item> findByCategory(String category) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("category", category)
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Item> items = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            items.add(doc.toObject(Item.class));
        }
        return items;
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = firestore.collection(COLLECTION_NAME).document(id).delete();
        writeResult.get();
    }
}