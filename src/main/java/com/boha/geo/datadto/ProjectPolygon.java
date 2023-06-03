package com.boha.geo.datadto;

import com.boha.geo.monitor.data.City;
import com.boha.geo.monitor.data.Position;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "projectPolygons")
public class ProjectPolygon {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String projectId;
    private String projectPolygonId;
    private String organizationId;
    private List<Position> positions;
    private String projectName;
    private String name;
    private String created;
    private List<City> nearestCities;
    private String userId;
            private String userName;
    private String translatedMessage;
    private String translatedTitle;



}
