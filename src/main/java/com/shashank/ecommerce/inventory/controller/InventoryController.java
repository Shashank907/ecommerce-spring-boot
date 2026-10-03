package com.shashank.ecommerce.inventory.controller;

import com.shashank.ecommerce.inventory.dto.InventoryDto;
import com.shashank.ecommerce.inventory.dto.UpdateInventoryRequestDto;
import com.shashank.ecommerce.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryDto> getInventory(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                inventoryService.getInventoryByProductId(productId)
        );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<InventoryDto> updateInventory(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateInventoryRequestDto request) {

        return ResponseEntity.ok(
                inventoryService.updateInventory(productId, request)
        );
    }
}