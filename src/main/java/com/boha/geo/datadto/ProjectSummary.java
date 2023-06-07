package com.boha.geo.datadto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ProjectSummary")

public class ProjectSummary {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String projectId;
    private long photos;
    private long videos;
    private long audios;
    private long schedules;
    private long projectPositions;
    private long projectPolygons;
    private int calculatedHourly;
    private int day;
    private int hour;
    private String startDate;
    private String endDate;
    private String batchId;

    private String date;
    private String projectName;
    private String organizationId;
    private String organizationName;


}
