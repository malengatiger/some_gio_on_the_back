package com.boha.geo.datadto;

import com.boha.geo.monitor.data.*;
import com.boha.geo.monitor.data.ActivityType;
import com.boha.geo.monitor.data.Audio;
import com.boha.geo.monitor.data.OrgMessage;
import com.boha.geo.monitor.data.Photo;
import com.boha.geo.monitor.data.Project;
import com.boha.geo.monitor.data.User;
import com.boha.geo.monitor.data.Video;
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
        Photo photo;
        Video video;
        Audio audio;
        User user;
        Project project;
        ProjectPosition projectPosition;
        ProjectPolygon projectPolygon;
        OrgMessage orgMessage;
        GeofenceEvent geofenceEvent;
        LocationRequest locationRequest;
        LocationResponse locationResponse;
        SettingsModel settingsModel;
        String userType;
        String translatedUserType;
}
