package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("ActivityModel")
public class ActivityModel {

        private String  _partitionKey;
        @Id
        private String  _id;
        String activityModelId;
        ActivityType activityType;
        String date;
        String userId;
        String userName;
        String userThumbnailUrl;
        String projectId;
        String projectName;
        String organizationName;
        String organizationId;
        String photo;
        String video;
        String audio;
        String user;
        String project;
        String projectPosition;
        String projectPolygon;
        String orgMessage;
        String geofenceEvent;
        String locationRequest;
        String locationResponse;
        String settingsModel;
        String userType;
        String translatedUserType;
}
