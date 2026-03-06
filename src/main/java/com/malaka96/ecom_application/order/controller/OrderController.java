package com.malaka96.ecom_application.order.controller;

import com.malaka96.ecom_application.order.dto.OrderResponse;
import com.malaka96.ecom_application.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/placeorder")
    public ResponseEntity<?> placeOrder(@RequestHeader("X-User-ID") Long userId){
        Optional<OrderResponse> orderResponse = orderService.placeOrder(userId);
        if(orderResponse.isEmpty()){
            return ResponseEntity.badRequest().build();
        }else{
            return ResponseEntity.ok(orderResponse);
        }
    }

}
