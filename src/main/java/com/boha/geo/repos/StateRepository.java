package com.boha.geo.repos;

import com.boha.geo.monitor.data.Audio;
import com.boha.geo.monitor.data.mcountry.State;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StateRepository extends MongoRepository<State, String> {

    List<State> findByCountryId(String countryId);

}
