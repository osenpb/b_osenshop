package com.osen.osenshop.payment_config;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    public Payment createPayment(PaymentRequest request) throws MPException, MPApiException {

        PaymentClient client = new PaymentClient();

        PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                .transactionAmount(BigDecimal.valueOf(request.transactionAmount()))
                .description(request.description())
                .installments(request.installments())
                .paymentMethodId(request.paymentMethodId())
                .token(request.token())
                .payer(
                        PaymentPayerRequest.builder()
                                .email(request.payer().email())
                                .build()
                )
                .build();
        return client.create(paymentCreateRequest);
    }
}