package com.boha.geo.datadto.mcountry;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("states")
public class State {
    private String stateId;
    private String countryId;
    private String name;
    private String countryName;
    @JsonProperty("state_code")
    private String stateCode;
    private double latitude;
    private double longitude;
}
