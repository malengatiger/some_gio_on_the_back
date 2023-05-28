package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "geofenceEvents")
public class GeofenceEvent {
    private String _partitionKey;
    @Id
    private String _id;

    private String status;
    private String geofenceEventId;
    private String date;
    private String projectPositionId;
    private String projectId;
    private String projectName;
    private String organizationId;
    private User user;
    private Position position;
    private String translatedMessage;
    private String translatedTitle;



}
