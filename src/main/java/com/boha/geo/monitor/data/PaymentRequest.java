package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("paymentRequests")
public class PaymentRequest {
    Amount amount;
    String payerReference;
    String beneficiaryReference;
    String externalReference;
    String beneficiaryName;
    String beneficiaryBankId;
    String beneficiaryAccountNumber;
    String merchant;
    String paymentRequestId;
    String organizationId;
    String subscriptionId;
    String date;
}
