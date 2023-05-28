package com.boha.geo.monitor.data;

import com.boha.geo.models.GioSubscription;
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
