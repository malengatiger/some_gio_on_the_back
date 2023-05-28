package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("pricing")
public class Pricing {
    private String _id;
    private String countryId;
    private String countryName;
    private String date;
    private double monthlyPrice;
    private double annualPrice;

}
