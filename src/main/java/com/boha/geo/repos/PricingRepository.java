package com.boha.geo.repos;

import com.boha.geo.monitor.data.AppError;
import com.boha.geo.monitor.data.Pricing;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PricingRepository extends MongoRepository<Pricing, String> {

    List<Pricing> findByCountryId(String countryId);

}
