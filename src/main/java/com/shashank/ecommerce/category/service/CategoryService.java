package com.shashank.ecommerce.category.service;

import com.shashank.ecommerce.category.dto.AddCategoryRequestDto;
import com.shashank.ecommerce.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(AddCategoryRequestDto categoryDto);

    List<CategoryDto> getAllCategories();

    CategoryDto getCategoryById(Long id);

    CategoryDto updateCategory(Long id, AddCategoryRequestDto requestDto);

    void deleteCategory(Long id);
}
