package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "userCounts")

public class UserCounts {
    private List<DataCounts> dataCounts;


}
