package com.youtube.e_commerce_backend.api.controller.order;

import com.youtube.e_commerce_backend.model.LocalUser;
import com.youtube.e_commerce_backend.model.WebOrder;
import com.youtube.e_commerce_backend.services.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<WebOrder> getOrders(@AuthenticationPrincipal LocalUser user) {
        return orderService.getOrdersByUser(user);
    }

}
