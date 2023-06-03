package com.boha.geo.datadto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "orgMessages")
public class OrgMessage {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String organizationId;
    private String projectId;
    private String userId;
    private String message;
    private String created;
    private String fcmRegistration;
    private String projectName;
    private String adminId;
    private String adminName;
    private String frequency;
    private String result;
    private String name;
    private String orgMessageId;
    private OrgMessage replyingTo;


}
