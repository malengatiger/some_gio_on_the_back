package com.boha.geo.repos;

import com.boha.geo.models.GioSubscription;
import com.boha.geo.monitor.data.Audio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GioSubscriptionRepository extends MongoRepository<GioSubscription, String> {

    List<GioSubscription> findByOrganizationId(String organizationId);

}
