package com.shashank.ecommerce.category.repository;

import com.shashank.ecommerce.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
