package org.example.ezyshop.service.impl;

import jakarta.transaction.Transactional;
import org.example.ezyshop.dto.variant.VariantDTO;
import org.example.ezyshop.dto.variant.VariantRequest;
import org.example.ezyshop.dto.variant.VariantResponse;
import org.example.ezyshop.entity.Product;
import org.example.ezyshop.entity.Variant;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.exception.RequetFailException;
import org.example.ezyshop.mapper.VariantMapper;
import org.example.ezyshop.repository.ProductRepository;
import org.example.ezyshop.repository.SizeRepository;
import org.example.ezyshop.repository.VariantRepository;
import org.example.ezyshop.service.FileStorageService;
import org.example.ezyshop.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class VariantServiceImpl implements VariantService {
    @Autowired
    VariantRepository repository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SizeRepository sizeRepository;

    FileStorageService fileStorageService;

    public VariantResponse getAllVariants(Long productId) {
        List<Variant> variants = repository.findByProductId(productId);
        List<VariantDTO> dtoList = variants.stream().map(VariantMapper.MAPPER::toDTO).toList();
        return new VariantResponse(true, 200).setDtoList(dtoList);
    }


    @Transactional
    public VariantResponse createVariant(VariantRequest request, MultipartFile file) {
        Product product = productRepository.findByIdAndIsDeletedFalse(request.getProductId());
        if (product == null) {
            throw new NotFoundException(false, 404, "Product not exists");
        }
        Variant variant = new Variant();
        variant.setAttribute(request.getAttribute());
        try {
            variant.setImageUrl(fileStorageService.storeFile(file));
        } catch (Exception e) {
            throw new RequetFailException("There was an error uploading the photo, please reselect the photo");
        }
        repository.save(variant);
        return new VariantResponse(true, 200).setDto(VariantMapper.MAPPER.toDTO(variant));
    }

    @Transactional
    public VariantResponse updateVariant(Long id, VariantRequest request) {
        Variant variant = repository.findByIdAndIsDeletedFalse(id);
        if (variant == null) {
            throw new NotFoundException(false, 404, "Variant not exists");
        }
        VariantMapper.MAPPER.updateVariantFromRequest(request, variant);
        repository.save(variant);
        return new VariantResponse(true, 200).setDto(VariantMapper.MAPPER.toDTO(variant));
    }

    @Transactional
    public VariantResponse delete(Long id) {
        Variant variant = repository.findByIdAndIsDeletedFalse(id);
        if (variant == null) {
            throw new NotFoundException(false, 404, "Variant not exists");
        }
        variant.setDeleted(true);
        repository.save(variant);
        return new VariantResponse(true, 200, "delete variant successfully");
    }
}
