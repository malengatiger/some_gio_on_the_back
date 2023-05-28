package com.boha.geo.repos;

import com.boha.geo.monitor.data.City;
import com.boha.geo.monitor.data.PaymentRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentRequestRepo extends MongoRepository<PaymentRequest, String> {
    public List<PaymentRequest> findByOrganizationId(String organizationId);
    public List<PaymentRequest> findByPaymentRequestId(String paymentRequestId);

}

