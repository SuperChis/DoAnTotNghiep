package org.example.ezyshop.service;

import org.example.ezyshop.dto.store.CreateStoreRequest;
import org.example.ezyshop.dto.store.StoreRequestState;
import org.example.ezyshop.dto.store.StoreResponse;

public interface StoreService {
    StoreResponse requestCreateStore(CreateStoreRequest request);
    StoreRequestState getStoreRequestState();
    StoreResponse getCreateStoreRequest();
    StoreResponse confirmCreateStoreRequest(Long id);
    StoreResponse getStoreInforByUser();
}
