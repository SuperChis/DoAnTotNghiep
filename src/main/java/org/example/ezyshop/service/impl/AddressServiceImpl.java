package org.example.ezyshop.service.impl;

import jakarta.transaction.Transactional;
import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.config.service.UserDetailsImpl;
import org.example.ezyshop.dto.address.AddressDTO;
import org.example.ezyshop.dto.address.AddressRequest;
import org.example.ezyshop.dto.address.AddressResponse;
import org.example.ezyshop.dto.user.UserDTO;
import org.example.ezyshop.entity.Address;
import org.example.ezyshop.entity.User;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.mapper.AddressMapper;
import org.example.ezyshop.mapper.UserMapper;
import org.example.ezyshop.repository.AddressRepository;
import org.example.ezyshop.repository.UserRepository;
import org.example.ezyshop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository repository;

    @Transactional
    public AddressResponse addAddress(AddressRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Address address = new Address()
                .setSpecificAddress(request.getSpecificAddress())
                .setWard(request.getWard())
                .setDistrict(request.getDistrict())
                .setProvince(request.getProvince())
                .setDefaultAddress(request.getDefaultAddress() != null ? request.getDefaultAddress() : false)
                .setIsDeleted(false)
                .setUser(user)
                .setNameCustomer(request.getNameCustomer())
                .setPhoneNumber(request.getPhoneNumber());
        address.setCreated(new Date());
        address.setLastUpdate(new Date());

        List<Address> addressList = repository.findByUserId(user.getId());
        if (request.getDefaultAddress() == Boolean.TRUE) {
            addressList.stream().map(a -> a.setDefaultAddress(false)).toList();
        }
        AddressDTO dto = AddressMapper.MAPPER.toDTO(address);
        String fullAddress = address.getProvince() + ", " + address.getDistrict() + ", " +  address.getWard();
        dto.setFullAddress(fullAddress);
        repository.save(address);

        return new AddressResponse(true, 201, "Address added successfully").setAddressDTO(dto);
    }

    @Override
    public AddressResponse getAddresses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        List<Address> addressList = repository.findByUserId(user.getId());
        List<AddressDTO> dtoList = addressList.stream().map(AddressMapper.MAPPER::toDTO).toList();
        dtoList.stream()
                .map(dto -> dto.setFullAddress(dto.getProvince() + ", " + dto.getDistrict() + ", " +  dto.getWard()))
                .toList();

        return new AddressResponse(true, 200).setAddressDTOS(dtoList);
    }

    @Transactional
    public AddressResponse updateAddress(AddressRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Address address = repository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException(false, 400, "Not found address"));

        List<Address> addressList = repository.findByUserId(user.getId());
        if (request.getDefaultAddress() == Boolean.TRUE) {
            addressList.stream()
                    .filter(a -> !Objects.equals(a.getId(), request.getId()))
                    .map(a -> a.setDefaultAddress(false))
                    .toList();
        }

        AddressMapper.MAPPER.updateAddressFromRequest(request,address);
        AddressDTO dto = AddressMapper.MAPPER.toDTO(address);
        String fullAddress = address.getProvince() + ", " + address.getDistrict() + ", " +  address.getWard();
        dto.setFullAddress(fullAddress);
        repository.save(address);
        return new AddressResponse(true, 200, "Address updated successfully")
                .setAddressDTO(dto);
    }

    @Transactional
    public BaseResponse deleteAddress(Long id) {
        Address address = repository.findByIdAndIsDefaultAddressFalse(id)
                .orElseThrow(() -> new NotFoundException(false, 400, "Not found address or address is default, could change it to un default"));
        address.setIsDeleted(true);
        repository.save(address);
        return new BaseResponse(true, 200, "Address deleted successfully");
    }

}
