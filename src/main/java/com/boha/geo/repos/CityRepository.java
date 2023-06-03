package com.boha.geo.repos;

import com.boha.geo.monitor.data.City;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CityRepository extends MongoRepository<City, String> {

    GeoResults<City> findByPositionNear(Point location, Distance distance);
    public City findByName(String name);
    public List<City> findByCountryId(String countryId);

    public List<City> findByProvince(String province);

}
