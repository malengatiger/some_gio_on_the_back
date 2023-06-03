package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "userCounts")

public class UserCounts {
    private String _partitionKey;
    @Id
    private String id;
    private String userId;
    private List<DataCounts> dataCounts;


}
