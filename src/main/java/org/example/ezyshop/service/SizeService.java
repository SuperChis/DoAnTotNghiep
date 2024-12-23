package org.example.ezyshop.service;

import org.example.ezyshop.dto.size.SizeDTO;
import org.example.ezyshop.dto.size.SizeRequest;
import org.example.ezyshop.dto.size.SizeResponse;
import org.example.ezyshop.entity.SizeEntity;
import org.example.ezyshop.entity.Variant;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.mapper.SizeMapper;

import java.util.List;

public interface SizeService {
    public SizeResponse getAllSizes(Long variantId);

    public SizeResponse createSize(SizeRequest sizeRequest);

    public SizeResponse updateSize(Long id, SizeRequest sizeRequest);

    public SizeResponse deleteSize(Long id);
}
