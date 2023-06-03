package com.boha.geo.datadto;

import com.boha.geo.monitor.data.Position;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("locationResponses")

public class LocationResponse {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String organizationId;
    private String userId;
    private String userName;
    private String date;
    private String organizationName;
    private String locationResponseId;
    private String requesterId;
    private String requesterName;
    private Position position;
    private String translatedMessage;
    private String translatedTitle;
}
