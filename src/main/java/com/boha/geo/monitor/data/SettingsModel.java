package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("settings")
public class SettingsModel {
    private String  _partitionKey;
    @Id
    private String  _id;
    private int distanceFromProject;
    private int photoSize;
    private int maxVideoLengthInSeconds;
    private int maxAudioLengthInMinutes;
    private int themeIndex;
    private int activityStreamHours;
    private int numberOfDays;
    private String settingsId;
    private String created;
    private String organizationId;
    private String projectId;
    private String userId;
    private String userName;
    private String userThumbnailUrl;
    private String locale;
    private String translatedMessage;
    private String translatedTitle;
    private int refreshRateInMinutes;
}
