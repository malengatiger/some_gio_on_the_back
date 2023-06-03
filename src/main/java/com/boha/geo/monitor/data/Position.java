package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
public class Position {
    String type;
    List<Double> coordinates;
    double latitude;
    double longitude;


}
