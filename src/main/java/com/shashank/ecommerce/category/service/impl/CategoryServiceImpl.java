
package com.shashank.ecommerce.category.service.impl;

import com.shashank.ecommerce.category.dto.AddCategoryRequestDto;
import com.shashank.ecommerce.category.dto.CategoryDto;
import com.shashank.ecommerce.category.entity.Category;
import com.shashank.ecommerce.category.repository.CategoryRepository;
import com.shashank.ecommerce.category.service.CategoryService;
import com.shashank.ecommerce.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(AddCategoryRequestDto request) {

        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException(
                    "Category already exists with name: "
                            + request.getName()
            );
        }

        Category category =
                modelMapper.map(request, Category.class);

        Category savedCategory =
                categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(category ->
                        modelMapper.map(category, CategoryDto.class))
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        )
                );

        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(
            Long id,
            AddCategoryRequestDto request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        )
                );

        if (categoryRepository.existsByNameAndIdNot(
                request.getName(), id)) {

            throw new IllegalArgumentException(
                    "Category already exists with name: "
                            + request.getName()
            );
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory =
                categoryRepository.save(category);

        return modelMapper.map(
                updatedCategory,
                CategoryDto.class
        );
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        )
                );

        categoryRepository.delete(category);
    }
}



