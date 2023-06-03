package com.boha.geo.datadto;

import com.boha.geo.models.GioSubscription;
import com.boha.geo.monitor.data.Organization;
import com.boha.geo.monitor.data.Project;
import com.boha.geo.monitor.data.ProjectPosition;
import com.boha.geo.monitor.data.SettingsModel;
import com.boha.geo.monitor.data.User;
import lombok.Data;

import java.util.List;

@Data
public class OrganizationRegistrationBag {
    private Organization organization;
    private User user;
    private SettingsModel settings;
    private Project project;
    private ProjectPosition projectPosition;
    private String date;
    private double latitude, longitude;
    private GioSubscription gioSubscription;

}
