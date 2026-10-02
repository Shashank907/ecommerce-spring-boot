package com.shashank.ecommerce.user.controller;

import com.shashank.ecommerce.user.dto.AddressDto;
import com.shashank.ecommerce.user.dto.CreateAddressRequestDto;
import com.shashank.ecommerce.user.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(
            @PathVariable Long userId,
            @RequestBody @Valid CreateAddressRequestDto request) {

        AddressDto createdAddress =
                addressService.createAddress(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdAddress);
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getUserAddresses(
            @PathVariable Long userId) {

        List<AddressDto> addresses =
                addressService.getUserAddresses(userId);

        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(
            @PathVariable Long id) {

        AddressDto address =
                addressService.getAddressById(id);

        return ResponseEntity.ok(address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long id) {

        addressService.deleteAddress(id);

        return ResponseEntity.noContent().build();
    }
}