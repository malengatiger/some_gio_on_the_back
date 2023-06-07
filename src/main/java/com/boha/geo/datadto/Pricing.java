package com.boha.geo.datadto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Pricing")
public class Pricing {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String  pricingId;
    private String countryId;
    private String countryName;
    private String date;
    private double monthlyPrice;
    private double annualPrice;

}
