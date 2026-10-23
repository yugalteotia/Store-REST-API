package com.yugal.store.payments;

import com.yugal.store.orders.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckOutSession createCheckoutSession(Order order, String websiteUrl);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
