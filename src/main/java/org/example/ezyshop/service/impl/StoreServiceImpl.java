package org.example.ezyshop.service.impl;

import org.example.ezyshop.config.service.UserDetailsImpl;
import org.example.ezyshop.dto.store.CreateStoreRequest;
import org.example.ezyshop.dto.store.StoreDTO;
import org.example.ezyshop.dto.store.StoreResponse;
import org.example.ezyshop.entity.Role;
import org.example.ezyshop.entity.StoreEntity;
import org.example.ezyshop.entity.User;
import org.example.ezyshop.enums.ERole;
import org.example.ezyshop.exception.AuthenticationFailException;
import org.example.ezyshop.exception.RequetFailException;
import org.example.ezyshop.mapper.StoreMapper;
import org.example.ezyshop.repository.RoleRepository;
import org.example.ezyshop.repository.StoreRepository;
import org.example.ezyshop.repository.UserRepository;
import org.example.ezyshop.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    private StoreRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public StoreResponse requestCreateStore(CreateStoreRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        StoreEntity store = new StoreEntity();
        store.setAddress(request.getAddress());
        store.setName(request.getName());
        store.setDescription(request.getDescription());
        store.setApproved(false);
        store.setUser(user);
        store.setCreated(new Date());
        store.setLastUpdate(new Date());
        repository.save(store);
        return new StoreResponse(true, 200, "Request Created Store Successfully. Please wait admin confirm your request.");
    }

    @Override
    public StoreResponse getCreateStoreRequest() {
        List<StoreEntity> storeList = repository.findAllRequestCreatedStore();
        List<StoreDTO> dtoList = storeList.stream()
                .map(StoreMapper.MAPPER::toDTO)
                .toList();
        return new StoreResponse(true, 200).setDtoList(dtoList);
    }

    @Override
    public StoreResponse confirmCreateStoreRequest(Long id) {
        StoreEntity store = repository.findStoreCanBeUpToStore(id);
        if (store == null) {
            throw new RequetFailException(false, 404, "Not found request to Upgrade to store");
        }
        store.setApproved(true);
        store.setDeleted(false);
        User user = store.getUser();
//        user.setStoreEntity(store);
        user.setStore(true);
        Set<Role> roles = user.getRoles();
        Role userRole = roleRepository.findByName(ERole.ROLE_STORE)
                .orElseThrow(() -> new AuthenticationFailException(false, 401, "Error: Role is not found"));
        roles.add(userRole);
        userRepository.save(user);
        repository.save(store);
        return new StoreResponse(true, 200, "this request was be confirmed");
    }

    @Override
    public StoreResponse getStoreInforByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        StoreEntity store = repository.findByUserId(user.getId());
        return new StoreResponse(true, 200)
                .setDto(StoreMapper.MAPPER.toDTO(store));
    }
}
