package com.boha.geo.monitor.services;


import com.boha.geo.monitor.data.*;
import com.boha.geo.repos.*;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.geo.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

;

@RequiredArgsConstructor
@Service
public class ListService {
    public static final Logger LOGGER = LoggerFactory.getLogger(ListService.class.getSimpleName());
    private static final String xx = E.COFFEE + E.COFFEE + E.COFFEE;
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    private final GeofenceEventRepository geofenceEventRepository;

    private final AppErrorRepository appErrorRepository;

    private final ActivityModelRepository activityModelRepository;

    private final ProjectSummaryRepository projectSummaryRepository;

    private final CountryRepository countryRepository;

    private final CityRepository cityRepository;

    private final OrganizationRepository organizationRepository;

    private final ProjectPolygonRepository projectPolygonRepository;

    private final ProjectRepository projectRepository;

    private final CommunityRepository communityRepository;

    private final SettingsModelRepository settingsModelRepository;

    private final UserRepository userRepository;

    private final MonitorReportRepository monitorReportRepository;

    private final QuestionnaireRepository questionnaireRepository;

    private final PhotoRepository photoRepository;

    private final ProjectAssignmentRepository projectAssignmentRepository;

    private final VideoRepository videoRepository;

    private final DataService dataService;

    private final AudioRepository audioRepository;

    private final ConditionRepository conditionRepository;


    private final FieldMonitorScheduleRepository fieldMonitorScheduleRepository;

    private final MongoTemplate mongoTemplate;

    static final double lat = 0.0144927536231884; // degrees latitude per mile
    static final double lon = 0.0181818181818182; // degrees longitude per mile

    public User findUserByEmail(String email) throws Exception {

        User user = userRepository.findByEmail(email);

        if (user != null) {
            LOGGER.info(E.GLOBE.concat(E.GLOBE).concat("findUserByEmail ... found: \uD83D\uDC24 " + G.toJson(user)));
        } else {
            LOGGER.info(E.ALIEN + "User " + email + " not found, probably not registered yet ".concat(E.NOT_OK));
        }
        return user;
    }

    public List<FieldMonitorSchedule> getProjectFieldMonitorSchedules(String projectId, String startDate, String endDate) {

        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<FieldMonitorSchedule> m = mongoTemplate.find(query, FieldMonitorSchedule.class);

        return m;
    }

    public List<FieldMonitorSchedule> getUserFieldMonitorSchedules(String userId) {

        List<FieldMonitorSchedule> m = fieldMonitorScheduleRepository.findByUserId(userId);

        return m;
    }

    public List<FieldMonitorSchedule> getOrgFieldMonitorSchedules(String organizationId, String startDate, String endDate) {
        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<FieldMonitorSchedule> mList = mongoTemplate.find(query, FieldMonitorSchedule.class);


        return mList;
    }

    public List<FieldMonitorSchedule> getMonitorFieldMonitorSchedules(String userId) {

        List<FieldMonitorSchedule> m = fieldMonitorScheduleRepository.findByFieldMonitorId(userId);

        return m;
    }

    public List<FieldMonitorSchedule> getAdminFieldMonitorSchedules(String userId) {

        List<FieldMonitorSchedule> m = fieldMonitorScheduleRepository.findByAdminId(userId);

        return m;
    }

    public List<Organization> getOrganizations() {

        List<Organization> mList = organizationRepository.findAll();
        return mList;
    }

    public List<City> getCitiesByCountry(String countryId) {

        return cityRepository.findByCountryId(countryId);
    }

    public List<Community> getCommunities() {

        List<Community> mList = communityRepository.findAll();

        return mList;
    }

    public List<Project> getProjects() {

        List<Project> mList = projectRepository.findAll();

        return mList;
    }


    public List<Organization> getCountryOrganizations(String countryId) {

        List<Organization> mList = organizationRepository.findByCountryId(countryId);

        return mList;
    }

    public Organization getOrganization(String organizationId) throws Exception {
        Query q = new Query();
        q.addCriteria(Criteria.where("organizationId").is(organizationId));

        List<Organization> orgs = mongoTemplate.find(q, Organization.class);
        if (!orgs.isEmpty()) {
            return orgs.get(0);
        } else {
            throw new Exception("Organization not found");
        }
    }

    public List<Photo> getProjectPhotos(String projectId, String startDate, String endDate) {

        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        return mongoTemplate.find(query, Photo.class);
    }

    public List<ProjectAssignment> getProjectAssignments(String projectId, String startDate, String endDate) {
        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<ProjectAssignment> mList = mongoTemplate.find(query, ProjectAssignment.class);
        return mList;
    }

    public List<ProjectAssignment> getUserProjectAssignments(String userId) {

        return projectAssignmentRepository.findByUserId(userId);
    }

    public List<ProjectAssignment> getOrganizationProjectAssignments(String organizationId) {

        return projectAssignmentRepository.findByOrganizationId(organizationId);
    }

    public List<ActivityModel> getOrganizationActivity(String organizationId, int hours) {
        LOGGER.info("getOrganizationActivity , hours: " + hours);
        DateTime then = DateTime.now().minusHours(hours);
        String date = then.toDateTimeISO().toString();
        LOGGER.info("getOrganizationActivity , date: " + date);
        Criteria criteria = new Criteria();
        criteria = criteria.and("organizationId").is(organizationId);
        criteria = criteria.and("date").gt(date);

        Query query = new Query(criteria);
        List<ActivityModel> activities = mongoTemplate.find(query, ActivityModel.class);
        LOGGER.info("getOrganizationActivity , activities: " + activities.size());

        return activities;
    }

    public List<ActivityModel> getProjectActivity(String projectId, int hours) {

        DateTime then = DateTime.now().minusHours(hours);
        String date = then.toDateTimeISO().toString();

        Criteria criteria = new Criteria();
        criteria = criteria.and("projectId").is(projectId);
        criteria = criteria.and("date").gt(date);

        Query query = new Query(criteria);
        List<ActivityModel> activities = mongoTemplate.find(query, ActivityModel.class);
        return activities;
    }
    public List<ActivityModel> getOrganizationActivity(String organizationId) {

        List<ActivityModel> activities = activityModelRepository
                .findByOrganizationId(organizationId);
        return activities;
    }

    public List<ActivityModel> getUserActivity(String userId, int hours) {

        DateTime then = DateTime.now().minusHours(hours);
        String date = then.toDateTimeISO().toString();

        Criteria criteria = new Criteria();
        criteria = criteria.and("userId").is(userId);
        criteria = criteria.and("date").gt(date);

        Query query = new Query(criteria);
        List<ActivityModel> activities = mongoTemplate.find(query, ActivityModel.class);
        return activities;
    }

    public DataBag getUserData(String userId, String startDate, String endDate) {
        DataBag bag = new DataBag();
        User user = userRepository.findByUserId(userId);
        List<Project> projects = projectRepository.findByOrganizationId(user.getOrganizationId());
        List<Photo> photos = getUserProjectPhotos(userId);
        List<Video> videos = getUserProjectVideos(userId);
        List<ProjectAssignment> assignments = getUserProjectAssignments(userId);

        List<ProjectPosition> projectPositions = getOrganizationProjectPositions(user.getOrganizationId(), startDate, endDate);
        List<ProjectPolygon> projectPolygons = getOrganizationProjectPolygons(user.getOrganizationId(), startDate, endDate);
        List<FieldMonitorSchedule> fieldMonitorSchedules = getOrgFieldMonitorSchedules(user.getOrganizationId(), startDate, endDate);
        List<User> users = getOrganizationUsers(user.getOrganizationId(), startDate, endDate);

        bag.setDate(DateTime.now().toDateTimeISO().toString());
        bag.setProjects(projects);
        bag.setFieldMonitorSchedules(fieldMonitorSchedules);
        bag.setProjectPositions(projectPositions);
        bag.setProjectPolygons(projectPolygons);
        bag.setPhotos(photos);
        bag.setVideos(videos);
        bag.setUsers(users);
        bag.setProjectAssignments(assignments);

        return bag;
    }

    public DataBag getProjectData(String projectId, String startDate, String endDate) {
        DataBag bag = new DataBag();
        Project project = projectRepository.findByProjectId(projectId);
        List<Project> projects = new ArrayList<>();
        if (project != null) {
            projects.add(project);
        }

        List<Photo> photos = getProjectPhotos(projectId, startDate, endDate);
        List<Video> videos = getProjectVideos(projectId, startDate, endDate);
        List<Audio> audios = getProjectAudios(projectId, startDate, endDate);
        List<ProjectAssignment> assignments = getProjectAssignments(projectId, startDate, endDate);
        List<ProjectPosition> projectPositions = getProjectPositions(projectId, startDate, endDate);
        List<ProjectPolygon> projectPolygons = getProjectPolygons(projectId, startDate, endDate);
        List<FieldMonitorSchedule> fieldMonitorSchedules = getProjectFieldMonitorSchedules(projectId, startDate, endDate);
        List<SettingsModel> settings = getProjectSettings(projectId);

        bag.setDate(DateTime.now().toDateTimeISO().toString());
        bag.setProjects(projects);
        bag.setFieldMonitorSchedules(fieldMonitorSchedules);
        bag.setProjectPositions(projectPositions);
        bag.setProjectPolygons(projectPolygons);
        bag.setPhotos(photos);
        bag.setVideos(videos);
        bag.setAudios(audios);
        bag.setSettings(settings);
        bag.setProjectAssignments(assignments);

        return bag;
    }

    public Photo findPhotoById(String photoId) {
        return photoRepository.findByPhotoId(photoId);
    }

    public Video findVideoById(String photoId) {
        return videoRepository.findByVideoId(photoId);
    }

    public Audio findAudioById(String photoId) {
        return audioRepository.findByAudioId(photoId);
    }

    public DataBag getOrganizationData(String organizationId, String startDate, String endDate, int activityStreamHours) throws Exception {
        long start = System.currentTimeMillis();

        LOGGER.info("\uD83C\uDF00 getOrganizationData .............................: ");

        List<User> users = getOrganizationUsers(organizationId);
        LOGGER.info("\uD83C\uDF00 users: " + users.size());

        List<Project> projects = getAllOrganizationProjects(organizationId);
        LOGGER.info("\uD83C\uDF00 projects: " + projects.size());


        List<GeofenceEvent> geos = getOrganizationGeofenceEvents(organizationId, startDate, endDate);
        LOGGER.info("\uD83C\uDF00 geofenceEvents: " + geos.size());

        List<Video> videos = getOrganizationVideos(organizationId, startDate, endDate);
        LOGGER.info("\uD83C\uDF00 videos: " + videos.size());

        List<Audio> audios = getOrganizationAudios(organizationId, startDate, endDate);
        LOGGER.info("\uD83C\uDF00 audios: " + audios.size());

        List<Photo> photos = getOrganizationPhotos(organizationId, startDate, endDate);
        LOGGER.info("\uD83C\uDF00 photos: " + photos.size());

        List<ActivityModel> models = getOrganizationActivity(organizationId, activityStreamHours);
        LOGGER.info("\uD83C\uDF00 activities: " + models.size());


        List<ProjectAssignment> assignments = getOrganizationProjectAssignments(organizationId);
        LOGGER.info("\uD83C\uDF00 assignments: " + assignments.size());

        List<ProjectPosition> projectPositions = getOrganizationProjectPositions(organizationId, startDate, endDate);
        LOGGER.info("\uD83C\uDF00 projectPositions: " + projectPositions.size());

        List<FieldMonitorSchedule> fieldMonitorSchedules = getOrgFieldMonitorSchedules(organizationId, startDate, endDate);
        LOGGER.info("\uD83C\uDF00 fieldMonitorSchedules: " + fieldMonitorSchedules.size());

        List<ProjectPolygon> polygons = getOrganizationProjectPolygons(organizationId, startDate, endDate);
        LOGGER.info("\uD83C\uDF00 polygons: " + polygons.size());

        DataBag bag = new DataBag();
        bag.setDate(DateTime.now().toDateTimeISO().toString());
        bag.setProjects(projects);
        bag.setFieldMonitorSchedules(fieldMonitorSchedules);
        bag.setProjectPositions(projectPositions);
        bag.setProjectPolygons(polygons);
        bag.setPhotos(photos);
        bag.setVideos(videos);
        bag.setAudios(audios);
        bag.setUsers(users);
        bag.setGeofenceEvents(geos);
        bag.setProjectAssignments(assignments);
        bag.setActivityModels(models);

        long end = System.currentTimeMillis();
        long ms = (end - start);
        double elapsed = Double.parseDouble("" + ms) / Double.parseDouble("1000");
        LOGGER.info("\uD83C\uDF00 getOrganizationData: returning bag: .........  elapsed time: " + elapsed + " seconds");
        return bag;
    }


    public List<ProjectSummary> getOrganizationSummary(
            String organizationId, String startDate, String endDate) throws Exception {

        List<ProjectSummary> list = dataService.createDailyOrganizationSummaries(organizationId, startDate, endDate);

        return list;
    }

    static final String mm = "" + E.ALIEN + E.ALIEN + E.ALIEN + " Zip Data: ";

    public DataCounts getOrganizationDataCounts(String organizationId, String startDate, String endDate, int activityStreamHours) throws Exception {
        long start = System.currentTimeMillis();

        DataCounts counts = new DataCounts();
        Query query = new Query();
        query.addCriteria(Criteria.where("organizationId")
                .is(organizationId));
        long projects = mongoTemplate.count(query, Project.class);
        long users = mongoTemplate.count(query, User.class);
        long projectPositions = mongoTemplate.count(query, ProjectPosition.class);
        long projectPolygons = mongoTemplate.count(query, ProjectPolygon.class);

        Query queryByDates = new Query();
        queryByDates.addCriteria(Criteria.where("organizationId")
                .is(organizationId).and("created").gte(startDate).lte(endDate));
        long photos = mongoTemplate.count(queryByDates, Photo.class);
        long videos = mongoTemplate.count(queryByDates, Video.class);
        long audios = mongoTemplate.count(queryByDates, Audio.class);
        long schedules = mongoTemplate.count(queryByDates, FieldMonitorSchedule.class);

        String actEndDate = DateTime.now().toDateTimeISO().toString();
        Date then = DateTime.now().toDateTimeISO().minusHours(activityStreamHours).toDate();
        String actStartDate = then.toString();
        Query queryByHours = new Query();
        queryByHours.addCriteria(Criteria.where("organizationId")
                .is(organizationId).and("date").gte(actStartDate).lte(actEndDate));
        long activities = mongoTemplate.count(queryByHours, ActivityModel.class);

        counts.setActivities(activities);
        counts.setOrganizationId(organizationId);
        counts.setUsers(users);
        counts.setPhotos(photos);
        counts.setVideos(videos);
        counts.setAudios(audios);
        counts.setProjects(projects);
        counts.setProjectPositions(projectPositions);
        counts.setProjectPolygons(projectPolygons);
        counts.setFieldMonitorSchedules(schedules);
        counts.setCreated(DateTime.now().toDateTimeISO().toString());

        long end = System.currentTimeMillis();
        long ms = (end - start);
        double elapsed = Double.parseDouble("" + ms) / Double.parseDouble("1000");
        String json = G.toJson(counts);

        LOGGER.info("\uD83C\uDF00 counts: " + json);
        LOGGER.info("\uD83C\uDF00 getOrganizationDataCounts: returning counts: .........  elapsed time: " + elapsed + " seconds");

        return counts;
    }

    public File getOrganizationDataZippedFile(String organizationId, String startDate, String endDate, int activityStreamHours) throws Exception {
        LOGGER.info(mm + " getOrganizationDataZippedFile ..............................");
        long start = System.currentTimeMillis();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);

        DataBag bag = getOrganizationData(organizationId, startDate, endDate, activityStreamHours);
        long end = System.currentTimeMillis();
        long ms = (end - start);
        double elapsed = Double.parseDouble("" + ms) / Double.parseDouble("1000");
        LOGGER.info(mm + E.RED_DOT + E.RED_DOT + " After get data: " +
                " elapsed: " + elapsed + " seconds");
        String json = G.toJson(bag);
        LOGGER.info(mm + E.RED_DOT + E.RED_DOT + " Before zip: " + decimalFormat.format(json.length()) + " bytes in file");
        File dir = new File("zipDirectory");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File zippedFile = new File(dir, "" + DateTime.now().getMillis() + ".zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zippedFile));
        ZipEntry e = new ZipEntry("dataBag");
        out.putNextEntry(e);

        byte[] data = json.getBytes();
        out.write(data, 0, data.length);
        out.closeEntry();

        out.close();
        end = System.currentTimeMillis();
        ms = (end - start);
        elapsed = Double.parseDouble("" + ms) / Double.parseDouble("1000");

        LOGGER.info(mm + E.RED_DOT + E.RED_DOT + " After zip: "
                + decimalFormat.format(zippedFile.length()) +
                " bytes, elapsed: " + elapsed + " seconds");
        return zippedFile;
    }

    public File getProjectDataZippedFile(String projectId, String startDate, String endDate) throws Exception {
        long start = System.currentTimeMillis();
        DataBag bag = getProjectData(projectId, startDate, endDate);
        String json = G.toJson(bag);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);

        LOGGER.info(mm + E.RED_DOT + E.RED_DOT + " Before zip: " + decimalFormat.format(json.length()) + " bytes in json");

        File dir = new File("zipDirectory");
        if (!dir.exists()) {
            boolean ok = dir.mkdir();
            LOGGER.info(mm + " Zip directory created: path: " + dir.getAbsolutePath() + " created: " + ok);
        }
        File zippedFile = new File(dir, "" + DateTime.now().getMillis() + ".zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zippedFile));
        ZipEntry e = new ZipEntry("dataBag");
        out.putNextEntry(e);

        byte[] data = json.getBytes();
        out.write(data, 0, data.length);
        out.closeEntry();

        out.close();
        long end = System.currentTimeMillis();
        long ms = (end - start);
        double elapsed = Double.parseDouble("" + ms) / Double.parseDouble("1000");

        LOGGER.info(mm + E.RED_DOT + E.RED_DOT + " After zip: "
                + decimalFormat.format(zippedFile.length()) + " bytes in file, elapsed: "
                + E.RED_APPLE + " " + elapsed + " seconds");
        return zippedFile;
    }

    public File getUserDataZippedFile(String userId, String startDate, String endDate) throws Exception {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        DataBag bag = getUserData(userId, startDate, endDate);
        String json = G.toJson(bag);
        LOGGER.info(mm + E.RED_DOT + E.RED_DOT + " getUserDataZippedFile: Before zip: " + decimalFormat.format(json.length()) + " bytes");

        File dir = new File("zipDirectory");
        if (!dir.exists()) {
            boolean ok = dir.mkdir();
            LOGGER.info(mm + " Zip directory has been created: path: " + dir.getAbsolutePath() + " created: " + ok);
        }
        File zippedFile = new File(dir, "" + DateTime.now().getMillis() + ".zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zippedFile));
        ZipEntry e = new ZipEntry("dataBag");
        out.putNextEntry(e);

        byte[] data = json.getBytes();
        out.write(data, 0, data.length);
        out.closeEntry();

        out.close();
        LOGGER.info(mm + E.RED_DOT + E.RED_DOT + " After zipping: " + decimalFormat.format(zippedFile.length()) + " bytes in file");
        return zippedFile;
    }

    public List<Photo> getUserProjectPhotos(String userId) {

        List<Photo> mList = photoRepository.findByUserId(userId);

        return mList;
    }

    public List<Video> getUserProjectVideos(String userId) {

        List<Video> mList = videoRepository.findByUserId(userId);

        return mList;
    }

    public List<ProjectSummary> getProjectSummaries(String projectId, String startDate, String endDate) {

        List<ProjectSummary> mList = projectSummaryRepository.findByProjectInPeriod(projectId, startDate, endDate);

        return mList;
    }

    public List<ActivityModel> getProjectActivityPeriod(String projectId, String startDate, String endDate) {

        List<ActivityModel> mList = activityModelRepository.findByProjectPeriod(projectId, startDate, endDate);

        return mList;
    }

    public List<ProjectSummary> getOrganizationSummaries(String organizationId, String startDate, String endDate) {

        List<ProjectSummary> mList = projectSummaryRepository.findByOrganizationInPeriod(organizationId, startDate, endDate);

        return mList;
    }

    public List<ActivityModel> getOrganizationActivityPeriod(String organizationId, String startDate, String endDate) {

        List<ActivityModel> mList = activityModelRepository.findByOrganizationPeriod(organizationId, startDate, endDate);

        return mList;
    }

    public List<ActivityModel> getUserActivityPeriod(String userId, String startDate, String endDate) {

        List<ActivityModel> mList = activityModelRepository.findByUserPeriod(userId, startDate, endDate);

        return mList;
    }

    public List<Audio> getUserProjectAudios(String userId, String startDate, String endDate) {
        Criteria c = Criteria.where("userId").is(userId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Audio> mList = mongoTemplate.find(query, Audio.class);

        return mList;
    }

    public List<Video> getProjectVideos(String projectId, String startDate, String endDate) {

        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Video> mList = mongoTemplate.find(query, Video.class);

        return mList;
    }

    public List<Audio> getProjectAudios(String projectId, String startDate, String endDate) {

        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Audio> mList = mongoTemplate.find(query, Audio.class);

        return mList;
    }

    public List<SettingsModel> getProjectSettings(String projectId) {

        List<SettingsModel> mList = settingsModelRepository.findByProjectId(projectId);

        return mList;
    }

    public List<Condition> getProjectConditions(String projectId, String startDate, String endDate) {

        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Condition> mList = mongoTemplate.find(query, Condition.class);

        return mList;
    }


    ProjectPositionRepository projectPositionRepository;

    public List<Project> findProjectsByLocation(String organizationId, double latitude, double longitude, double radiusInKM) {


        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(radiusInKM, Metrics.KILOMETERS);
        if (projectPositionRepository == null) {
            return new ArrayList<>();
        }
        List<ProjectPosition> projects = projectPositionRepository.findByPositionNear(point, distance);

        List<Project> fList = new ArrayList<>();
        for (ProjectPosition projectPosition : projects) {

            if (projectPosition.getOrganizationId().equalsIgnoreCase(organizationId)) {
                Project p = projectRepository.findByProjectId(projectPosition.getProjectId());
                fList.add(p);
            }
        }
        HashMap<String, Project> map = new HashMap<>();
        for (Project project : fList) {
            map.put(project.getProjectId(), project);
        }
        fList = map.values().stream().toList();
        return fList;
    }

    public List<City> findCitiesByLocation(double latitude, double longitude, double radiusInKM) {
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(radiusInKM, Metrics.KILOMETERS);
        GeoResults<City> cities = cityRepository.findByPositionNear(point, distance);

        List<City> mList = new ArrayList<>();
        if (cities == null) {
            return mList;
        }

        for (GeoResult<City> city : cities) {
            mList.add(city.getContent());
        }
        return mList;
    }


    MongoClient mongoClient;

    public int countPhotosByProject(String projectId) {

        List<Photo> list = photoRepository.findByProjectId(projectId);
        return list.size();
    }

    public ProjectSummary getCountsByProject(String projectId) {

        if (photoRepository == null || projectPositionRepository == null) {
            val pc = new ProjectSummary();
            pc.setPhotos(0);
            pc.setDate(new DateTime().toDateTimeISO().toString());
            return pc;
        }
        int photos = photoRepository.findByProjectId(projectId).size();
        int videos = videoRepository.findByProjectId(projectId).size();
        int audios = audioRepository.findByProjectId(projectId).size();
        int schedules = fieldMonitorScheduleRepository.findByProjectId(projectId).size();
        int positions = projectPositionRepository.findByProjectId(projectId).size();
        int polygons = projectPolygonRepository.findByProjectId(projectId).size();

        Project project = projectRepository.findByProjectId(projectId);
        val pc = new ProjectSummary();
        pc.setPhotos(photos);
        pc.setDate(new DateTime().toDateTimeISO().toString());
        pc.setVideos(videos);
        pc.setProjectId(projectId);
        pc.setOrganizationId(project.getOrganizationId());
        pc.setAudios(audios);
        pc.setSchedules(schedules);
        pc.setProjectName(project.getName());
        pc.setOrganizationName(project.getOrganizationName());
        pc.setProjectPositions(positions);
        pc.setProjectPolygons(polygons);

        return pc;
    }

    public UserCounts getCountsByUser(String userId) {


        return null;
    }

    public int countVideosByProject(String projectId) {
        if (videoRepository == null) {
            return 0;
        }
        List<Video> videos = videoRepository.findByProjectId(projectId);
        return videos.size();
    }

    public int countPhotosByUser(String userId) {
        if (photoRepository == null) {
            return 0;
        }
        List<Photo> photos = photoRepository.findByUserId(userId);
        return photos.size();
    }

    public int countVideosByUser(String userId) {

        if (videoRepository == null) {
            return 0;
        }

        List<Video> list = videoRepository.findByUserId(userId);
        int res = list.size();
        LOGGER.info("..... videos found: " + res);
        return res;
    }

    public List<ProjectPosition> findProjectPositionsByLocation(String organizationId, double latitude, double longitude, double radiusInKM) {
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(radiusInKM, Metrics.KILOMETERS);
//        if (projectPositionRepository == null) {
//            return new ArrayList<>();
//        }

        NearQuery nearQuery = NearQuery.near(point).maxDistance(50, Metrics.MILES);

        GeoResults<ProjectPosition> positions = mongoTemplate.geoNear(nearQuery, ProjectPosition.class);
//        List<ProjectPosition> positions = projectPositionRepository.findByPositionNear(point, distance);

        List<ProjectPosition> mPositions = new ArrayList<>();
        if (positions == null) {
            return mPositions;
        }
        for (GeoResult<ProjectPosition> position : positions) {
            if (position.getContent().getOrganizationId().equalsIgnoreCase(organizationId)) {
                mPositions.add(position.getContent());
            }
        }

        return mPositions;
    }

    public List<ProjectPosition> getProjectPositions(String projectId, String startDate, String endDate) {
        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<ProjectPosition> mList = mongoTemplate.find(query, ProjectPosition.class);

        return mList;
    }

    public List<ProjectPolygon> getProjectPolygons(String projectId, String startDate, String endDate) {
        Criteria c = Criteria.where("projectId").is(projectId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<ProjectPolygon> mList = mongoTemplate.find(query, ProjectPolygon.class);

        return mList;
    }

    public List<ProjectPosition> getOrganizationProjectPositions(String organizationId, String startDate, String endDate) {
        List<ProjectPosition> mList = new ArrayList<>();

        try {
            Criteria c = Criteria.where("organizationId").is(organizationId)
                    .and("created").gte(startDate).lte(endDate);
            Query query = new Query(c);
            mList = mongoTemplate.find(query, ProjectPosition.class);

            return mList;
        } catch (Exception e) {
            if (e instanceof DataAccessResourceFailureException) {
                LOGGER.info("Retrying failed request ,,,");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    getOrganizationProjectPositions(organizationId, startDate, endDate);
                }
            }
        }
        return mList;
    }

    public List<ProjectPosition> getOrganizationProjectPositions(String organizationId) {

        if (projectPositionRepository == null) {
            return new ArrayList<>();
        }
        List<ProjectPosition> mList = projectPositionRepository.findByOrganizationId(organizationId);

        return mList;
    }

    public List<ProjectPolygon> getOrganizationProjectPolygons(String organizationId, String startDate, String endDate) {
        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<ProjectPolygon> mList = mongoTemplate.find(query, ProjectPolygon.class);

        return mList;
    }

    public List<ProjectPolygon> getOrganizationProjectPolygons(String organizationId) {
        List<ProjectPolygon> mList = projectPolygonRepository.findByOrganizationId(organizationId);
        return mList;
    }

    public List<GeofenceEvent> getGeofenceEventsByProjectPosition(String projectPositionId) {

        List<GeofenceEvent> events = geofenceEventRepository.findByProjectPositionId(projectPositionId);
        return events;
    }

    public List<GeofenceEvent> getGeofenceEventsByOrganization(String organizationId) {

        List<GeofenceEvent> events = geofenceEventRepository.findByOrganizationId(organizationId);
        return events;
    }

    public List<City> getNearbyCities(double latitude, double longitude, double radiusInKM) {

        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(radiusInKM, Metrics.KILOMETERS);
        GeoResults<City> cities = cityRepository.findByPositionNear(point, distance);

        if (cities == null) {
            return new ArrayList<>();
        }
        List<City> mList = new ArrayList<>();
        for (GeoResult<City> city : cities) {
            mList.add(city.getContent());
        }
        return mList;
    }

    public List<Project> getOrganizationProjects(String organizationId, String startDate, String endDate) {

        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Project> mList = mongoTemplate.find(query, Project.class);

        return mList;
    }

    public List<Project> getAllOrganizationProjects(String organizationId) {

        return projectRepository.findByOrganizationId(organizationId);
    }
    public List<Photo> getAllOrganizationPhotos(String organizationId) {

        return photoRepository.findByOrganizationId(organizationId);
    } public List<Video> getAllOrganizationVideos(String organizationId) {

        return videoRepository.findByOrganizationId(organizationId);
    }
    public List<Audio> getAllOrganizationAudios(String organizationId) {

        return audioRepository.findByOrganizationId(organizationId);
    }
    public List<User> getAllOrganizationUsers(String organizationId) {

        return userRepository.findByOrganizationId(organizationId);
    }

    public List<ProjectPosition> getAllOrganizationPositions(String organizationId) {

        return projectPositionRepository.findByOrganizationId(organizationId);
    }

    public List<ProjectPolygon> getAllOrganizationPolygons(String organizationId) {

        return projectPolygonRepository.findByOrganizationId(organizationId);
    }
    public List<SettingsModel> getOrganizationSettings(String organizationId) {

        List<SettingsModel> mList = settingsModelRepository.findByOrganizationId(organizationId);

        return mList;
    }


    public List<User> getOrganizationUsers(String organizationId, String startDate, String endDate) {
        List<User> filteredList = new ArrayList<>();
        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<User> mList = mongoTemplate.find(query, User.class);
        for (User user : mList) {
            if (user.getActive() == 0) {
                filteredList.add(user);
            }
        }
        return filteredList;
    }

    public List<User> getOrganizationUsers(String organizationId) {
        List<User> filteredList = new ArrayList<>();
        List<User> mList = userRepository.findByOrganizationId(organizationId);
        for (User user : mList) {
            if (user.getActive() == 0) {
                filteredList.add(user);
            }
        }
        return filteredList;
    }

    public List<GeofenceEvent> getOrganizationGeofenceEvents(String organizationId, String startDate, String endDate) {

        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("date").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<GeofenceEvent> mList = mongoTemplate.find(query, GeofenceEvent.class);
        return mList;
    }

    public List<Photo> getOrganizationPhotos(String organizationId, String startDate, String endDate) {

        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Photo> mList = mongoTemplate.find(query, Photo.class);

        return mList;
    }

    public List<Video> getOrganizationVideos(String organizationId, String startDate, String endDate) {

        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Video> mList = mongoTemplate.find(query, Video.class);

//        List<Video> xList = videoRepository.findByOrganizationId(organizationId);

        return mList;
    }

    public List<Audio> getOrganizationAudios(String organizationId, String startDate, String endDate) {

        Criteria c = Criteria.where("organizationId").is(organizationId)
                .and("created").gte(startDate).lte(endDate);
        Query query = new Query(c);
        List<Audio> mList = mongoTemplate.find(query, Audio.class);

//        List<Audio> xList = audioRepository.findByOrganizationId(organizationId);


        return mList;
    }

    public List<User> getUsers() {

        List<User> mList = userRepository.findAll();
        return mList;
    }


    public User getUserById(String userId) {

        User user = userRepository.findByUserId(userId);
        if (user == null) {
            return null;
        }
        if (user.getActive() > 0) {
            return null;
        }

        return user;
    }

    public List<City> getCities() {

        List<City> mList = cityRepository.findAll();

        return mList;
    }

    public List<Community> findCommunitiesByCountry(String countryId) {

        List<Community> mList = communityRepository.findByCountryId(countryId);
        return mList;
    }

    public List<Questionnaire> getQuestionnairesByOrganization(String organizationId) {

        List<Questionnaire> list = questionnaireRepository.findByOrganizationId(organizationId);
        return list;
    }

    public List<Project> findProjectsByOrganization(String organizationId) {

        return projectRepository.findByOrganizationId(organizationId);
    }

    public List<AppError> findAppErrorsByOrganization(String organizationId) {

        return appErrorRepository.findByOrganizationId(organizationId);
    }

    public List<AppError> findAppErrorsByUser(String userId) {

        return appErrorRepository.findByUserId(userId);
    }

    //
    public List<com.boha.geo.monitor.data.mcountry.Country> getCountries() {

        List<com.boha.geo.monitor.data.mcountry.Country> mList = countryRepository.findAll();

        return mList;
    }


}
