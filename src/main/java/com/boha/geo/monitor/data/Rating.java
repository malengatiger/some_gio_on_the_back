package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ratings")
public class Rating {
    private String _partitionKey;
    @Id
    private String id;
    private String ratingId;
    private String projectId;
    private String projectName;
    private int ratingCode;
    private String created;
    private String userId;
    private String userName;
    private String organizationId;
    private String videoId;
    private String audioId;
    private String photoId;
    private String remarks;
    private Position position;

    public Rating() {
    }

}
