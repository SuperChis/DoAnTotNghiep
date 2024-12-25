package org.example.ezyshop.service.impl;

import org.example.ezyshop.dto.size.SizeDTO;
import org.example.ezyshop.dto.size.SizeRequest;
import org.example.ezyshop.dto.size.SizeResponse;
import org.example.ezyshop.entity.SizeEntity;
import org.example.ezyshop.entity.Variant;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.mapper.SizeMapper;
import org.example.ezyshop.repository.ProductRepository;
import org.example.ezyshop.repository.SizeRepository;
import org.example.ezyshop.repository.VariantRepository;
import org.example.ezyshop.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {
    @Autowired
    private SizeRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantRepository variantRepository;

    public SizeResponse getAllSizes(Long variantId) {
        List<SizeDTO> sizeDTOS =  repository.findAllByVariantId(variantId)
                .stream()
                .map(SizeMapper.INSTANCE::toDTO)
                .toList();
        return new SizeResponse(true, 200).setSizeList(sizeDTOS);
    }

    public SizeResponse createSize(SizeRequest sizeRequest) {
        Variant variant = variantRepository.findByIdAndIsDeletedFalse(sizeRequest.getVariantId());
        if (variant == null) {
            throw new NotFoundException(false, 404, "Variant not exists");
        }
        SizeEntity sizeEntity = SizeMapper.INSTANCE.toEntity(sizeRequest);
        sizeEntity.setCreated(new Date());
        sizeEntity.setLastUpdate(new Date());
        SizeEntity savedEntity = repository.save(sizeEntity);
        return new SizeResponse(true, 200).setDto(SizeMapper.INSTANCE.toDTO(savedEntity));
    }

    public SizeResponse updateSize(Long id, SizeRequest sizeRequest) {
        SizeEntity existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Size not found with id: " + id));
        SizeMapper.INSTANCE.updateEntityFromRequest(sizeRequest, existingEntity);
        existingEntity.setLastUpdate(new Date());
        SizeEntity updatedEntity = repository.save(existingEntity);
        return new SizeResponse(true, 200).setDto(SizeMapper.INSTANCE.toDTO(updatedEntity));
    }

    public SizeResponse deleteSize(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Size not found with id: " + id);
        }
        repository.deleteById(id);
        return new SizeResponse(true, 200, "deleted successfully");
    }
}
