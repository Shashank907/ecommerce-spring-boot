package com.shashank.ecommerce.wishlist.service.impl;

import com.shashank.ecommerce.exception.ResourceNotFoundException;
import com.shashank.ecommerce.product.entity.Product;
import com.shashank.ecommerce.product.repository.ProductRepository;
import com.shashank.ecommerce.user.entity.User;
import com.shashank.ecommerce.user.repository.UserRepository;
import com.shashank.ecommerce.wishlist.dto.WishlistDto;
import com.shashank.ecommerce.wishlist.dto.WishlistItemDto;
import com.shashank.ecommerce.wishlist.entity.Wishlist;
import com.shashank.ecommerce.wishlist.entity.WishlistItem;
import com.shashank.ecommerce.wishlist.repository.WishlistItemRepository;
import com.shashank.ecommerce.wishlist.repository.WishlistRepository;
import com.shashank.ecommerce.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public WishlistDto createWishlist(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        if (wishlistRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException(
                    "Wishlist already exists for user id: " + userId);
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);

        return mapToDto(savedWishlist);
    }

    @Override
    public WishlistDto getWishlist(Long userId) {

        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Wishlist not found for user id: " + userId));

        return mapToDto(wishlist);
    }

    @Override
    @Transactional
    public void deleteWishlist(Long userId) {

        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Wishlist not found for user id: " + userId));

        // Delete wishlist items first
        List<WishlistItem> items =
                wishlistItemRepository.findByWishlistId(wishlist.getId());

        wishlistItemRepository.deleteAll(items);

        // Then delete the wishlist
        wishlistRepository.delete(wishlist);
    }

    @Override
    @Transactional
    public WishlistItemDto addItem(
            Long userId,
            Long productId) {

        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Wishlist not found for user id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId));

        if (wishlistItemRepository.existsByWishlistIdAndProductId(
                wishlist.getId(), productId)) {

            throw new IllegalArgumentException(
                    "Product is already in the wishlist");
        }

        WishlistItem item = new WishlistItem();
        item.setWishlist(wishlist);
        item.setProduct(product);

        WishlistItem savedItem = wishlistItemRepository.save(item);

        return mapItemToDto(savedItem);
    }

    @Override
    public List<WishlistItemDto> getWishlistItems(Long userId) {

        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Wishlist not found for user id: " + userId));

        return wishlistItemRepository.findByWishlistId(wishlist.getId())
                .stream()
                .map(this::mapItemToDto)
                .toList();
    }

    @Override
    @Transactional
    public void removeItem(
            Long userId,
            Long productId) {

        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Wishlist not found for user id: " + userId));

        WishlistItem item = wishlistItemRepository
                .findByWishlistIdAndProductId(
                        wishlist.getId(),
                        productId
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found in wishlist"));

        wishlistItemRepository.delete(item);
    }

    private WishlistDto mapToDto(Wishlist wishlist) {

        WishlistDto dto = new WishlistDto();

        dto.setId(wishlist.getId());
        dto.setUserId(wishlist.getUser().getId());
        dto.setCreatedAt(wishlist.getCreatedAt());
        dto.setUpdatedAt(wishlist.getUpdatedAt());

        return dto;
    }

    private WishlistItemDto mapItemToDto(WishlistItem item) {

        WishlistItemDto dto = new WishlistItemDto();

        dto.setId(item.getId());
        dto.setWishlistId(item.getWishlist().getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductPrice(item.getProduct().getPrice());
        dto.setImageUrl(item.getProduct().getImageUrl());
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}