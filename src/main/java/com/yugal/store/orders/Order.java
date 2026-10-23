package com.yugal.store.orders;

import com.yugal.store.carts.Cart;
import com.yugal.store.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    public Order(User customer, PaymentStatus paymentStatus, BigDecimal cartsTotalPrice) {
        this.customer = customer;
        this.status = paymentStatus;
        this.totalPrice = cartsTotalPrice;
    }

    public static Order fromCart(Cart cart, User customer) {
//        var order = Order.builder()
//                .customer(customer)
//                .status(OrderStatus.PENDING)
//                .totalPrice(cart.getCartsTotalPrice())
//                .build();
        var order = new Order(customer, PaymentStatus.PENDING, cart.getCartsTotalPrice());

//        var orderItems = order.getOrderItems();
//        cart.getCartItems().forEach(cartItem -> {
//            orderItems.add(OrderItem.builder()
//                    .order(order)
//                    .product(cartItem.getProduct())
//                    .unitPrice(cartItem.getProduct().getPrice())
//                    .quantity(cartItem.getQuantity())
//                    .totalPrice(cartItem.getTotalPrice())
//                    .build());
//        });

        cart.getCartItems().forEach(cartItem -> {
            var orderItem = new OrderItem(order, cartItem.getProduct(),
                    cartItem.getProduct().getPrice(),
                    cartItem.getQuantity(),
                    cartItem.getTotalPrice());
            order.orderItems.add(orderItem);
        });

        return order;
    }

    public boolean isPlacedBy(User customer) {
        return this.customer.equals(customer);
    }
}