package com.boha.geo.datadto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("ProjectAssignment")
public class ProjectAssignment {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String organizationId;
    private String projectId;
    private String projectName;
    private String userId;
    private String userName;
    private String adminId;
    private String adminName;
    private String projectAssignmentId;
    private String date;
    private String updated;
    private int active;

}
