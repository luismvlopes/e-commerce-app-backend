package com.youtube.e_commerce_backend.services;

import com.youtube.e_commerce_backend.model.LocalUser;
import com.youtube.e_commerce_backend.model.WebOrder;
import com.youtube.e_commerce_backend.model.dao.WebOrderDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final WebOrderDAO webOrderDAO;

    public OrderService(WebOrderDAO webOrderDAO) {
        this.webOrderDAO = webOrderDAO;
    }

    public List<WebOrder> getOrdersByUser(LocalUser user) {
        return webOrderDAO.findByUser(user);
    }

}
