package org.example.ezyshop.service.impl;

import org.example.ezyshop.config.service.UserDetailsImpl;
import org.example.ezyshop.dto.store.CreateStoreRequest;
import org.example.ezyshop.dto.store.StoreResponse;
import org.example.ezyshop.entity.StoreEntity;
import org.example.ezyshop.entity.User;
import org.example.ezyshop.repository.StoreRepository;
import org.example.ezyshop.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    private StoreRepository repository;


    @Override
    public StoreResponse requestCreateStore(CreateStoreRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        StoreEntity store = new StoreEntity();
        store.setAddress(request.getAddress());
        store.setName(request.getName());
        store.setApproved(false);
        store.setDescription(request.getDescription());
        repository.save(store);
        return new StoreResponse(true, 200, "Request Created Store Successfully. Please wait admin confirm your request.");
    }

    @Override
    public StoreResponse getCreateStoreRequest(CreateStoreRequest request) {
        List<StoreEntity> storeList = repository.findAllRequestCreatedStore();
        return null;
    }

    @Override
    public StoreResponse confirmCreateStoreRequest(CreateStoreRequest request) {
        return null;
    }

    @Override
    public StoreResponse getStore() {
        return null;
    }
}
