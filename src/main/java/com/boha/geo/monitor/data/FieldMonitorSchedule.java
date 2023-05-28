package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "fieldMonitorSchedules")
public class FieldMonitorSchedule {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String adminId;
    private String projectId;
    private String fieldMonitorScheduleId;
    private String date;
    private String organizationId;
    private String fieldMonitorId;
    private String fieldMonitorName;
    private String projectName;
    private String organizationName;
    private String userId;
    private int perDay;
    private int perWeek;
    private int perMonth;


}
