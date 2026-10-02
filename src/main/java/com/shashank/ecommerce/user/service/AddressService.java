package com.shashank.ecommerce.user.service;

import com.shashank.ecommerce.user.dto.AddressDto;
import com.shashank.ecommerce.user.dto.CreateAddressRequestDto;

import java.util.List;

public interface AddressService {

    AddressDto createAddress(Long userId, CreateAddressRequestDto request);

    List<AddressDto> getUserAddresses(Long userId);

    AddressDto getAddressById(Long id);

    void deleteAddress(Long id);
}