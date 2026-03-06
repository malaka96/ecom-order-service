package com.malaka96.ecom_application.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItemRequest {
    private Long productId;
    private Integer quantity;
}
