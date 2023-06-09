package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("PaymentRequest")
public class PaymentRequest {
    private String  _partitionKey;
    @Id
    private String  _id;
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
