package com.shashank.ecommerce.category.service.impl;

import ch.qos.logback.core.model.Model;
import com.shashank.ecommerce.category.dto.AddCategoryRequestDto;
import com.shashank.ecommerce.category.dto.CategoryDto;
import com.shashank.ecommerce.category.entity.Category;
import com.shashank.ecommerce.category.repository.CategoryRepository;
import com.shashank.ecommerce.category.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Transactional
   public CategoryDto createCategory(AddCategoryRequestDto categoryDto){


        Category category = modelMapper.map(categoryDto, Category.class);

        Category newCategory = categoryRepository.save(category);

        return modelMapper.map(newCategory, CategoryDto.class);

    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> modelMapper.map(category,CategoryDto.class))
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category=categoryRepository
                .findById(id)
                .orElseThrow(()->new IllegalArgumentException("Category not found with id: " + id));
        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(Long id, AddCategoryRequestDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + id));

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category updatedCategory = categoryRepository.save(category);

        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }

}
