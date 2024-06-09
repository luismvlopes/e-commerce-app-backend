package com.youtube.e_commerce_backend.model.dao;

import com.youtube.e_commerce_backend.model.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductDAO extends ListCrudRepository<Product, Long> {
}
