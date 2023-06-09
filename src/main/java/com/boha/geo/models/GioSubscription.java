package com.boha.geo.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("GioSubscription")
public class GioSubscription {
    private String subscriptionId;
    private String organizationId;
    private String organizationName;
    private String date;
    private long intDate;
    private int subscriptionType;
    private String user;
    private String updated;
    private long intUpdated;
    private int active = 0;
    private String _id;

}
