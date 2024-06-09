package com.youtube.e_commerce_backend.services;

import com.youtube.e_commerce_backend.model.Product;
import com.youtube.e_commerce_backend.model.dao.ProductDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getProducts() {
        return productDAO.findAll();
    }



}
