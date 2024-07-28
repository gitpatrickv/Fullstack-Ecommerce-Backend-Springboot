package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.model.response.PaymentResponse;
import com.practice.fullstackbackendspringboot.service.PaymentService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;
    @Override
    public PaymentResponse paymentLink(double totalAmount,String paymentMethod) throws StripeException {

        if(paymentMethod.equals(StringUtil.Cash)){
            return null;
        }

        Stripe.apiKey=stripeSecretKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(StringUtil.SuccessUrl)
                .setCancelUrl(StringUtil.CancelUrl)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(StringUtil.Php)
                                .setUnitAmount((long) totalAmount*100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(StringUtil.Shopee).build())
                                .build())
                        .build())
                .build();

        Session session =  Session.create(params);

        PaymentResponse response = new PaymentResponse();
        response.setPayment_url(session.getUrl());

        return response;
    }
}
