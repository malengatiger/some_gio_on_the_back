package com.boha.geo.datadto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("locationRequests")
public class LocationRequest {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String organizationId;
    private String created;
    private String requesterId;
    private String requesterName;
    private String userId;
    private String userName;
    private String organizationName;
    private String translatedMessage;
    private String translatedTitle;
}
