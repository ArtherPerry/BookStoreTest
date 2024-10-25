package com.example.bookstorecopy.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${stripe.apiKey}")
    private String stripeApiKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeApiKey;
    }

    public PaymentIntent createPaymentIntent(int amount){
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) amount*100) // amount in cents
                    .setCurrency("usd")
                    .addPaymentMethodType("card")
                    .build();
            return PaymentIntent.create(params);
        }catch (StripeException e){
            throw new RuntimeException("Failed to create payment intent: " + e.getMessage(),e);
        }
    }

}
