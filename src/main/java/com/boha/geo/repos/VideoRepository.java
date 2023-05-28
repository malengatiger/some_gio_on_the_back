package com.boha.geo.repos;

import com.boha.geo.monitor.data.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VideoRepository extends MongoRepository<Video, String> {

    List<Video> findByProjectId(String projectId);
    List<Video> findByUserId(String userId);
    List<Video> findByOrganizationId(String organizationId);

    Video findByVideoId(String videoId);

//    @Query(value = "{userId: ?0}", count = true)
//    long countByUser(String userId);
//    @Query(value = "{projectId: ?0,  created: { $gte: ?1, $lt: ?2 } }")
//    List<Video> getByProjectPeriod(String projectId, String startDate, String endDate);
//
//    @Query(value = "{userId: ?0,  created: { $gte: ?1, $lt: ?2 } }")
//    List<Video> getByUserPeriod(String userId, String startDate, String endDate);
//
//    @Query(value = "{organizationId: ?0,  created: { $gte: ?1, $lt: ?2 } }")
//    List<Video> getByOrganizationPeriod(String organizationId, String startDate, String endDate);
//
//    @Query(value = "{projectId: ?0,  created: { $gt: ?1 } }", count = true)
//    long countByTimeAndProject(String projectId, String created);
//
//    @Query(value = "{organizationId: ?0,  created: { $gt: ?1 } }", count = true)
//    long countByTimeAndOrganization(String organizationId, String created);


}
