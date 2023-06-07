package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Organization")
public class Organization {
    private String _partitionKey;
    @Id
    private String _id;
    private String name;
    private String countryName;
    private String countryId;
    private String organizationId;
    private String created;


}
