package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("settings")
public class SettingsModel {
    private int distanceFromProject;
    private int photoSize;
    private int maxVideoLengthInSeconds;
    private int maxAudioLengthInMinutes;
    private int themeIndex;
    private int activityStreamHours;
    private int numberOfDays;
    private String settingsId, created, organizationId,
            projectId, userId, userName, userThumbnailUrl, locale;
    private String translatedMessage;
    private String translatedTitle;
}
