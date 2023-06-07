package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("LocationRequest")
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
