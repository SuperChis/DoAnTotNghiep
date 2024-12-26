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

import java.util.Date;
import java.util.List;

@Service
public class VariantServiceImpl implements VariantService {
    @Autowired
    VariantRepository repository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SizeRepository sizeRepository;

    @Autowired
    FileStorageService fileStorageService;

    public VariantResponse getAllVariants(Long productId) {
        List<Variant> variants = repository.findByProductId(productId);
        List<VariantDTO> dtoList = variants.stream().map(VariantMapper.MAPPER::toDTO).toList();
        return new VariantResponse(true, 200).setDtoList(dtoList);
    }


    @Transactional
    public VariantResponse createVariant(VariantRequest request) {
        Product product = productRepository.findByIdAndIsDeletedFalse(request.getProductId());
        if (product == null) {
            throw new NotFoundException(false, 404, "Product not exists");
        }
        Variant variant = new Variant();
        variant.setAttribute(request.getAttribute());
        variant.setProduct(product);
        variant.setCreated(new Date());
        variant.setLastUpdate(new Date());
        variant.setDeleted(false);

        String url;
        try {
            url = fileStorageService.storeFile(request.getFile());
        } catch (RequetFailException e) {
            throw e;
        } catch (Exception e) {
            throw new RequetFailException("upload error");
        }
        variant.setImageUrl(url);

        repository.save(variant);
        return new VariantResponse(true, 200).setDto(VariantMapper.MAPPER.toDTO(variant));
    }

    @Transactional
    public VariantResponse addVariantImage(Long id, MultipartFile file) {
        Variant variant = repository.findByIdAndIsDeletedFalse(id);
        String url;
        try {
            url = fileStorageService.storeFile(file);
        } catch (RequetFailException e) {
            throw e;
        } catch (Exception e) {
            throw new RequetFailException("upload error");
        }
        variant.setImageUrl(url);
        variant.setLastUpdate(new Date());
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
        variant.setLastUpdate(new Date());
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
