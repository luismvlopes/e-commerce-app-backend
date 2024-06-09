package com.youtube.e_commerce_backend.model.dao;

import com.youtube.e_commerce_backend.model.LocalUser;
import com.youtube.e_commerce_backend.model.WebOrder;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WebOrderDAO extends ListCrudRepository<WebOrder, Long> {
    List<WebOrder> findByUser(LocalUser user);
}
