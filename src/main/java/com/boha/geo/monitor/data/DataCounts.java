package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("dataCounts")
public class DataCounts {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String organizationId;
    private String projectId;
    private String userId;
    private long projects;
    private long users;
    private long activities;
    private long photos;
    private long audios;
    private long videos;
    private long projectPositions;
    private long projectPolygons;
    private long fieldMonitorSchedules;
    private String created;
    private String startDate;
    private String endDate;
    private int activityStreamHours;
}
