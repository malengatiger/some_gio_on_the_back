package com.boha.geo.datadto;

import com.boha.geo.monitor.data.DataCounts;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "UserCounts")

public class UserCounts {
    private String _partitionKey;
    @Id
    private String id;
    private String userId;
    private List<DataCounts> dataCounts;


}
