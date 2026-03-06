package com.malaka96.ecom_application.order.service;

import com.malaka96.ecom_application.order.enums.OrderStatus;
import com.malaka96.ecom_application.order.model.CartItem;
import com.malaka96.ecom_application.order.model.Order;
import com.malaka96.ecom_application.order.model.OrderItem;
import com.malaka96.ecom_application.order.dto.OrderItemDTO;
import com.malaka96.ecom_application.order.dto.OrderResponse;
import com.malaka96.ecom_application.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartItemService cartItemService;
//    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> placeOrder(Long userId) {
        // validate for cart items
        List<CartItem> existingCartItems = cartItemService.getCartItems(userId);
        if(existingCartItems.isEmpty()){
            return Optional.empty();
        }
//        // validate for user
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if(userOptional.isEmpty()){
//            return Optional.empty();
//        }
//        User user = userOptional.get();
        // calculate total price
        BigDecimal totalPrice = existingCartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        Order order = Order.builder()
                .userId(userId)
                .status(OrderStatus.PENDING)
                .totalAmount(totalPrice)
                .build();

        List<OrderItem> orderItems = existingCartItems.stream()
                .map(item -> OrderItem.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .order(order)
                        .build())
                .toList();

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        // clear the cart
        cartItemService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order savedOrder) {
        return OrderResponse.builder()
                .id(savedOrder.getId())
                .totalAmount(savedOrder.getTotalAmount())
                .status(savedOrder.getStatus())
                .createdAt(savedOrder.getCreatedAt())
                .items(savedOrder.getItems().stream()
                        .map(orderItem -> OrderItemDTO.builder()
                                .id(orderItem.getId())
                                .productId(orderItem.getProductId())
                                .quantity(orderItem.getQuantity())
                                .price(orderItem.getPrice())
                                .build()).toList())
                .build();
    }
}
