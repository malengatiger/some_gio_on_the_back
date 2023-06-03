package com.boha.geo.datadto;

import com.boha.geo.monitor.data.City;
import com.boha.geo.monitor.data.Photo;
import com.boha.geo.monitor.data.Position;
import com.boha.geo.monitor.data.Video;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "communities")
public class Community {
    private String  _partitionKey;
    @Id
    private String  _id;
    private String name;
    private String communityId;
    private String countryId;
    private int population;
    private String countryName;
    private List<Position> polygon;
    private List<Photo> photos;
    private List<Video> videos;
    private List<City> nearestCities;

    public Community() {
    }


}
