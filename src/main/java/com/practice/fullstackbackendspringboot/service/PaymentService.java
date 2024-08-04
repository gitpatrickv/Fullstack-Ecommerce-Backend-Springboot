package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.response.PaymentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentResponse paymentLink(double totalAmount, String paymentMethod) throws StripeException;
}
