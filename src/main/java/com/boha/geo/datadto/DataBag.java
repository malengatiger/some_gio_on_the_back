package com.boha.geo.datadto;

import com.boha.geo.monitor.data.*;
import com.boha.geo.monitor.data.Audio;
import com.boha.geo.monitor.data.Photo;
import com.boha.geo.monitor.data.User;
import com.boha.geo.monitor.data.Video;
import lombok.Data;

import java.util.List;

@Data
public class DataBag {
    private List<Photo> photos;
    private List<Video> videos;
    private List<Audio> audios;
    private List<FieldMonitorSchedule> fieldMonitorSchedules;
    private List<ProjectPosition> projectPositions;
    private List<ProjectPolygon> projectPolygons;

    private List<Project> projects;
    private List<ProjectAssignment> projectAssignments;
    private List<User> users;
    private List<SettingsModel> settings;
    private List<ActivityModel> activityModels;
    private List<GeofenceEvent> geofenceEvents;

    private String date;

}
