package com.malaka96.ecom_application.order.service;

import com.malaka96.ecom_application.order.model.CartItem;
import com.malaka96.ecom_application.order.dto.CartItemRequest;
import com.malaka96.ecom_application.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public boolean addToCart(Long userId, CartItemRequest cartItemRequest){
//        Optional<Product> productOptional = productRepository.findById(cartItemRequest.getProductId());
//        if(productOptional.isEmpty())
//            return false;
//        Product product = productOptional.get();
//
//        if(product.getStockQuantity() < cartItemRequest.getQuantity())
//            return false;
//
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if(userOptional.isEmpty())
//            return false;
//        User user = userOptional.get();

        CartItem exisistingCartItem = cartItemRepository.findByUserIdAndProductId(userId, cartItemRequest.getProductId());
        if(exisistingCartItem != null){
            // update quantity
            exisistingCartItem.setQuantity(exisistingCartItem.getQuantity() + cartItemRequest.getQuantity());
            exisistingCartItem.setPrice(exisistingCartItem.getPrice().add(BigDecimal.valueOf(1000.)));
            cartItemRepository.save(exisistingCartItem);
        }else{
            // add new item
            CartItem cartItem = CartItem.builder()
                    .userId(userId)
                    .productId(cartItemRequest.getProductId())
                    .quantity(cartItemRequest.getQuantity())
                    .price(BigDecimal.valueOf(1000.00))
                    .build();
            cartItemRepository.save(cartItem);
        }
        return true;

    }

    public boolean deleteItemFromCart(Long userId, Long productId){
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if(cartItem != null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCartItems(Long userId){
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(Long userId) {
//        userRepository.findById(Long.valueOf(userId)).ifPresent(cartItemRepository::deleteByUser);
        cartItemRepository.deleteByUserId(userId);
    }
}
