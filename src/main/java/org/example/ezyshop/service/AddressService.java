package org.example.ezyshop.service;

import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.dto.address.AddressRequest;
import org.example.ezyshop.dto.address.AddressResponse;

import java.util.List;

public interface AddressService {
    public AddressResponse addAddress(AddressRequest request);
    public AddressResponse getAddresses();
    public BaseResponse updateAddress(AddressRequest request);
    public BaseResponse deleteAddress(Long id);
}
