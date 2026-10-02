package com.shashank.ecommerce.user.service.impl;

import com.shashank.ecommerce.exception.ResourceNotFoundException;
import com.shashank.ecommerce.user.dto.AddressDto;
import com.shashank.ecommerce.user.dto.CreateAddressRequestDto;
import com.shashank.ecommerce.user.entity.Address;
import com.shashank.ecommerce.user.entity.User;
import com.shashank.ecommerce.user.repository.AddressRepository;
import com.shashank.ecommerce.user.repository.UserRepository;
import com.shashank.ecommerce.user.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public AddressDto createAddress(
            Long userId,
            CreateAddressRequestDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        ));

        Address address = new Address();

        address.setUser(user);
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setAddressType(request.getAddressType());
        address.setIsDefault(
                request.getIsDefault() != null
                        ? request.getIsDefault()
                        : false
        );

        Address savedAddress = addressRepository.save(address);

        return mapToDto(savedAddress);
    }

    @Override
    public List<AddressDto> getUserAddresses(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return addressRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public AddressDto getAddressById(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id: " + id
                        ));

        return mapToDto(address);
    }

    @Override
    public void deleteAddress(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id: " + id
                        ));

        addressRepository.delete(address);
    }

    private AddressDto mapToDto(Address address) {

        AddressDto dto = modelMapper.map(address, AddressDto.class);

        dto.setUserId(address.getUser().getId());

        return dto;
    }
}