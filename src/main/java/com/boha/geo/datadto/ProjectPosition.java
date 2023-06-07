package com.boha.geo.datadto;

import com.boha.geo.monitor.data.City;
import com.boha.geo.monitor.data.PlaceMark;
import com.boha.geo.monitor.data.Position;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "ProjectPosition")
public class ProjectPosition {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String projectId;
    private String projectPositionId;
    private String organizationId;
    private Position position;
    private String projectName;
    private String caption;
    private String name;
    private String created;
    private PlaceMark placemark;
    private List<City> nearestCities;
    private String userId;
    private String userName;
    private String possibleAddress;
    private String translatedMessage;
    private String translatedTitle;


    public ProjectPosition() {
    }

}
