package com.boha.geo.datadto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "Project")
public class Project {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String projectId;
    private String name;
    private String organizationId;
    private String description;
    private String organizationName;
    private double monitorMaxDistanceInMetres;
    private String created;
    private String translatedMessage;
    private String translatedTitle;
    private String imageUrl;

}
