package com.boha.geo.monitor.utils;

import com.boha.geo.monitor.data.*;
import com.boha.geo.monitor.services.DataService;
import com.boha.geo.monitor.services.ListService;
import com.boha.geo.repos.*;
import com.boha.geo.services.MongoService;
import com.boha.geo.services.UserService;
import com.boha.geo.util.E;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;


@Service
public class MongoGenerator {
    private static final Logger LOGGER = Logger.getLogger(MongoGenerator.class.getSimpleName());
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;

    public MongoGenerator(UserService userService, MongoService mongoService, CountryRepository countryRepository, CityRepository cityRepository,
                          UserRepository userRepository, CommunityRepository communityRepository, DataService dataService, ListService listService, MongoClient mongoClient,
                          OrganizationRepository organizationRepository,
                          ProjectRepository projectRepository, ProjectPositionRepository projectPositionRepository,
                          FieldMonitorScheduleRepository fieldMonitorScheduleRepository) {
        this.userService = userService;
        this.mongoService = mongoService;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.communityRepository = communityRepository;
        this.dataService = dataService;
        this.listService = listService;
        this.mongoClient = mongoClient;
        this.organizationRepository = organizationRepository;
        this.projectRepository = projectRepository;
        this.projectPositionRepository = projectPositionRepository;
        this.fieldMonitorScheduleRepository = fieldMonitorScheduleRepository;
        LOGGER.info(xx+ " MongoGenerator is up and good ");
    }


    private final UserService userService;
    private final MongoService mongoService;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    private final CommunityRepository communityRepository;

    private final DataService dataService;

    private static final double latitudeSandton = -26.10499958, longitudeSandton = 28.052499;
    private static final double latitudeHarties = -25.739830374, longitudeHarties = 27.892996428;
    private static final double latitudeLanseria = -25.934042, longitudeLanseria = 27.929928;

    private static final double latitudeRandburg = -26.093611, longitudeRandburg = 28.006390;
    private static final double latitudeRosebank = -26.140499438, longitudeRosebank = 28.037666516;
    private static final double latitudeJHB = -26.195246, longitudeJHB = 28.034088;
    // -19.0169211 29.1528018
    private static final double latitudeZA = -28.4792625, longitudeZA = 24.6727135;
    private static final double latitudeZIM = -19.0169211, longitudeZIM = 29.1528018;

    public void generate(int numberOfOrgs) throws Exception {
        LOGGER.info(E.DICE + E.DICE +E.DICE
                + " -------- Generation starting .... ");
        try {
            List<com.boha.geo.monitor.data.mcountry.Country> countries = countryRepository.findAll();


            LOGGER.info(E.DICE + E.DICE + E.DICE
                    + " -------- Delete users, generate "+numberOfOrgs+" organizations and communities ... ");

            deleteAuthUsers();
            generateOrganizations(numberOfOrgs);
            generateCommunities();

            mongoService.initializeIndexes();

        } catch (Exception e) {
            LOGGER.severe(E.RED_DOT+E.RED_DOT+ "We have a problem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        LOGGER.info(E.DICE + E.DICE + E.DICE + E.DICE +
                " ........ Generation completed!! "
                + E.RED_APPLE);
    }


    private final ListService listService;

    private final MongoClient mongoClient;



    private static final int BATCH_SIZE = 600;

    private int cnt = 0;

    private void writeBatches(List<City> mCities) {
        int rem = mCities.size() % BATCH_SIZE;
        int pages = mCities.size() / BATCH_SIZE;
        if (rem > 0) {
            pages++;
        }
        int index = 0;
        cnt = 0;
        for (int i = 0; i < pages; i++) {

            List<City> mBatch = new ArrayList<>();
            for (int j = 0; j < BATCH_SIZE; j++) {
                try {
                    mBatch.add(mCities.get(index));
                    index++;

                } catch (Exception e) {
                }
            }
            try {
                List<City> mList = (List<City>) cityRepository.saveAll(mBatch);
                LOGGER.info(E.PEAR + E.PEAR + E.PEAR + E.FERN + E.FERN
                        + E.PEAR + "...... Written to mongo:" +
                        E.RED_APPLE + " CITY BATCH #" + i
                        + " batch size: " + mList.size());
                cnt += mList.size();

            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.info(E.NOT_OK + " City addition fell down hard, BATCH #" + i);
            }
        }


    }


    private final OrganizationRepository organizationRepository;
    private final ProjectRepository projectRepository;

    private final ProjectPositionRepository projectPositionRepository;

    private void generateOrganizations(int numberOfOrgs) throws Exception {

        firstNames = userService.getFirstNames();
        lastNames = userService.getLastNames();

        addFirstNames();
        addLastNames();

        setOrgNames();
        setCommunityNames();
        setProjectNames();

        List<com.boha.geo.monitor.data.mcountry.Country> countries = countryRepository.findAll();
        List<Organization> organizations = new ArrayList<>();
        //Generate orgs for South Africa
        com.boha.geo.monitor.data.mcountry.Country country = countries.get(0);
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS).concat("generateOrganizations: "
                .concat(country.getName()).concat(" ")
                .concat(E.FLOWER_YELLOW)));

        HashMap<String, String> hMap = new HashMap<>();
        while (hMap.keySet().size() < numberOfOrgs) {
            String name = getRandomOrgName();
            if (!hMap.containsKey(name)) {
                hMap.put(name, name);
            }
        }
        Collection<String> orgNames = hMap.values();
        for (String name : orgNames) {
            Organization org1 = new Organization();
            org1.setName(name);
            org1.setOrganizationId(UUID.randomUUID().toString());
            org1.setCreated(DateTime.now().toDateTime().toString());
            org1.setCountryName(country.getName());

            Organization id1 = organizationRepository.insert(org1);
            organizations.add(id1);
            LOGGER.info(E.RAINBOW.concat(E.RAINBOW.concat("Organization has been generated ".concat(org1.getName()
                    .concat(" ").concat(E.RED_APPLE)))));
        }

        LOGGER.info(E.LEAF + E.LEAF + "Organizations generated: " + organizations.size());

//        generateProjects();
        generateUsers(true);
        generateFieldMonitorSchedules();
    }

    private static final double monitorMaxDistanceInMetres = 500.0;

    @Autowired
    private FieldMonitorScheduleRepository fieldMonitorScheduleRepository;


    private void generateFieldMonitorSchedules() {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS +
                E.RAIN_DROPS.concat(" generateFieldMonitorSchedules ...")));

        Iterator<Organization> iterOrg= organizationRepository.findAll().iterator();

        Iterator<ProjectPosition> iterProj = projectPositionRepository.findAll().iterator();
        List<Organization> orgs = new ArrayList<>();

        while (iterOrg.hasNext()) {
            Organization u = iterOrg.next();
            orgs.add(u);
        }
        for (Organization org : orgs) {
            LOGGER.info("\n\n" + E.BROCCOLI + E.BROCCOLI + E.BROCCOLI + E.BROCCOLI + E.RED_APPLE +"Organization Schedules: " + org.getName());
            List<User> users = userRepository.findByOrganizationId(org.getOrganizationId());
            List<Project> projects = projectRepository.findByOrganizationId(org.getOrganizationId());
            LOGGER.info(E.BROCCOLI + E.BROCCOLI + E.PRETZEL + org.getName() + " Users: " + users.size());
            LOGGER.info(E.BROCCOLI + E.BROCCOLI + E.PRETZEL +org.getName() + " Projects: " + projects.size());
            for (User u : users) {
                for (Project p : projects) {
                    FieldMonitorSchedule schedule = new FieldMonitorSchedule();
                    schedule.setAdminId(null);
                    schedule.setFieldMonitorId(u.getUserId());
                    schedule.setDate(new DateTime().toDateTimeISO().toString());
                    schedule.setFieldMonitorScheduleId(UUID.randomUUID().toString());
                    schedule.setOrganizationId(u.getOrganizationId());
                    schedule.setOrganizationName(u.getOrganizationName());
                    schedule.setFieldMonitorName(u.getName());
                    schedule.setPerDay(3);
                    schedule.setProjectId(p.getProjectId());
                    schedule.setProjectName(p.getName());
                    fieldMonitorScheduleRepository.insert(schedule);
                    LOGGER.info(E.BROCCOLI + E.BROCCOLI
                            + "fieldMonitorSchedule added, " +E.RED_APPLE+ " fieldMonitor: " + u.getName() + " - "
                            + " project: " + p.getName());
                }
            }
        }

    }


    private String getRandomCellphone() {

        return String.valueOf(random.nextInt(9)) +
                random.nextInt(9) +
                random.nextInt(9) +
                random.nextInt(9) +
                random.nextInt(9) +
                random.nextInt(9) +
                random.nextInt(9) +
                random.nextInt(9) +
                random.nextInt(9) +
                random.nextInt(9);
    }

    private void generateUsers(boolean eraseExistingUsers) throws Exception {
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS + E.RAIN_DROPS.concat(" Generating Users ...")));
        List<Organization> organizations = organizationRepository.findAll();
        if (eraseExistingUsers) {
            deleteAuthUsers();
        }
        addFirstNames();
        addLastNames();
        setCommunityNames();
        setOrgNames();

        int cnt = 0;

        for (Organization organization : organizations) {
            for (int i = 0; i < 5; i++) {
                String name = getRandomFirstName() + " " + getRandomLastName();
                if (i == 0) {
                    buildUser(organization, name, "org", Type.USER_TYPE_ORG_ADMINISTRATOR);
                    cnt++;
                }

                if (i == 1 || i == 2 || i == 3) {
                    buildUser(organization, name, "monitor", Type.USER_TYPE_FIELD_MONITOR);
                    cnt++;
                }

                if (i == 4) {
                    buildUser(organization, name, "exec", Type.USER_TYPE_EXECUTIVE);
                    cnt++;
                }
            }
        }
        LOGGER.info(E.PEAR + E.PEAR + E.PEAR + " Users added : " + cnt);
    }

    private void buildUser(Organization org, String userName, String prefix, String userType) throws Exception {

        String email = buildEmail(prefix);
        User mUser = new User();
        mUser.setName(getRandomFirstName() + " " + getRandomLastName());
        mUser.setOrganizationId(org.getOrganizationId());
        mUser.setOrganizationName(org.getName());
        mUser.setCellphone(getRandomCellphone());
        mUser.setEmail(email);
        mUser.setCreated(new DateTime().toDateTimeISO().toString());
        mUser.setUserType(userType);
        mUser.setName(userName);
        mUser.setUserId(UUID.randomUUID().toString());
        mUser.setPassword("pass123");

        User u = dataService.createUser(mUser);
        u.setUserId(u.getUserId());
        u.setPassword(null);

        LOGGER.info(E.FERN + E.FERN +
                " User saved on MongoDB and Firebase auth: "
                + G.toJson(u) + E.FERN + E.FERN + u.getName());

    }

    private String buildEmail(String prefix) {
        String s1 = alphabet[random.nextInt(alphabet.length)];
        String s2 = alphabet[random.nextInt(alphabet.length)];
        String s3 = alphabet[random.nextInt(alphabet.length)];
        return prefix.toLowerCase() + "." + s1.toLowerCase() + s2.toLowerCase() + s3.toLowerCase()
                + "@monitor.com".toLowerCase();
    }

    private final String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private void generateCommunities() throws Exception {

        List<com.boha.geo.monitor.data.mcountry.Country> countries = countryRepository.findAll();
        com.boha.geo.monitor.data.mcountry.Country country = null;
        for (com.boha.geo.monitor.data.mcountry.Country country1 : countries) {
            if (country1.getName().contains("South Africa")) {
                country = country1;
            }
        }
        if (country == null) {
            throw new Exception("South Africa is not here, Bud!");
        }
        setCommunityNames();
        LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS + E.RAIN_DROPS
                .concat(" Generating " + communities.size() + " Communities ...")));
        for (String name : communities) {

            Community cs = new Community();
            cs.setCommunityId(UUID.randomUUID().toString());
            cs.setName(name);
            cs.setNearestCities(new ArrayList<>());
            cs.setPhotos(new ArrayList<>());
            cs.setPopulation(getPopulation());
            cs.setCountryName("South Africa");
            cs.setVideos(new ArrayList<>());
            Community sComm = communityRepository.insert(cs);
            LOGGER.info(E.RAIN_DROPS.concat(E.RAIN_DROPS + E.RAIN_DROPS
                    .concat(" Community Generated " + E.LEMON + E.LEMON +
                            sComm.getName() + " " + E.LEMON + " on mongodb database "
                            + E.RED_APPLE + E.RED_APPLE + E.RED_APPLE)));
        }
    }

    public static final String testProjectDesc = "This is the description of a truly transformational project that will change the lives of thousands of people." +
            " The infrastructure built will enhance quality of life and provide opportunities for entrepreneurs and job seekers";

    private int getPopulation() {
        int x = random.nextInt(100);
        return x == 0 ? 100000 : x * 20000;
    }

    private List<String> firstNames = new ArrayList<>();
    private List<String> lastNames = new ArrayList<>();
    private final List<String> organizationNames = new ArrayList<>();
    private final List<String> communities = new ArrayList<>();
    private final List<String> projectNames = new ArrayList<>();

    private final Random random = new Random(System.currentTimeMillis());

    private String getRandomFirstName() {
        return firstNames.get(random.nextInt(firstNames.size() - 1));
    }

    private String getRandomLastName() {
        return lastNames.get(random.nextInt(lastNames.size() - 1));
    }

    private String getRandomOrgName() {
        return organizationNames.get(random.nextInt(organizationNames.size() - 1));
    }

    //orgadmin1596705856490@monitor.com
    private void setOrgNames() {
        organizationNames.clear();
        organizationNames.add("Madibeng Municipality");
        organizationNames.add("Sithole RoadBuilders Ltd");
        organizationNames.add("KK Projects Inc.");
        organizationNames.add("Gauteng Housing Agency");
        organizationNames.add("Construction Monitors Pty Ltd");
        organizationNames.add("Construction Surveillance Agency");
        organizationNames.add("Peterson Construction Ltd");
        organizationNames.add("Nelson PK Infrastructure Ltd");
        organizationNames.add("Community Project Monitors Ltd");
        organizationNames.add("Afro Management & Monitoring Pty Ltd");
        organizationNames.add("Roberts InfraCon Lt");
        organizationNames.add("Rental Housing Monitoring");
    }

    private void setCommunityNames() {
        communities.clear();
        communities.add("Freedom Corner");
        communities.add("Marhumbi Community");
        communities.add("Victory Community Settlement");
        communities.add("Informal Dreams Community");
        communities.add("Jerusalema Groove");

    }

    private void addFirstNames() {
        firstNames.add("Mmaphefo");
        firstNames.add("Nomonde");
        firstNames.add("Nolwazi");
        firstNames.add("Bokgabane");
        firstNames.add("Thabiso");
        firstNames.add("Thabo");
        firstNames.add("Peter");
        firstNames.add("Nelson");
        firstNames.add("David");
        firstNames.add("Maria");
        firstNames.add("Ntozo");
        firstNames.add("Roberta");
        firstNames.add("Dineo");
        firstNames.add("Mokone");
        firstNames.add("Letlotlo");
        firstNames.add("Kgabi");
        firstNames.add("Malenga");
        firstNames.add("Msapa");
        firstNames.add("Musa");
        firstNames.add("Rogers");
        firstNames.add("Portia");
        firstNames.add("Sylvester");
        firstNames.add("David");
        firstNames.add("Nokwanda");
        firstNames.add("Nozipho");
        firstNames.add("Mantoa");
        firstNames.add("Dennis");
        firstNames.add("Vusi");
        firstNames.add("Kenneth");
        firstNames.add("Nana");
        firstNames.add("Vuyelwa");
        firstNames.add("Vuyo");
        firstNames.add("Helen");
        firstNames.add("Buti");
        firstNames.add("Craig");
        firstNames.add("David");
        firstNames.add("Cyril");
        firstNames.add("Shimange");
        firstNames.add("Bra Z");

    }

    private void addLastNames() {
        lastNames.add("Maluleke");
        lastNames.add("Baloyi");
        lastNames.add("Mthembu");
        lastNames.add("Sithole");
        lastNames.add("Rabane");
        lastNames.add("Mhinga");
        lastNames.add("Ngoasheng");
        lastNames.add("Mokone");
        lastNames.add("Mokoena");
        lastNames.add("Jones");
        lastNames.add("van der Merwe");
        lastNames.add("Nkosi");
        lastNames.add("Maringa");
        lastNames.add("Marule");
        lastNames.add("Fakude");
        lastNames.add("Maroleng");
        lastNames.add("Ntini");
        lastNames.add("Mathebula");
        lastNames.add("Buthelezi");
        lastNames.add("Zulu");
        lastNames.add("Khumalo");
        lastNames.add("Pieterse");
        lastNames.add("Makhatini");
        lastNames.add("Ndlovu");
        lastNames.add("Masemola");
        lastNames.add("Sono");
        lastNames.add("Hlungwane");
        lastNames.add("Makhubela");
        lastNames.add("Mthombeni");
    }

    private void setProjectNames() {
        projectNames.clear();
        projectNames.add("Road Renovation Project");
        projectNames.add("New Road Project");
        projectNames.add("Sanitation Plant Project");
        projectNames.add("School Renovation Project");
        projectNames.add("New School Project");
        projectNames.add("Community Centre Construction");
        projectNames.add("Sport Facility Construction");

    }


    public List<ExportedUserRecord> getAuthUsers() throws Exception {
        // Start listing users from the beginning, 1000 at a time.
        List<ExportedUserRecord> mList = new ArrayList<>();
        ApiFuture<ListUsersPage> future = FirebaseAuth.getInstance().listUsersAsync(null);
        ListUsersPage page = future.get();
        while (page != null) {
            for (ExportedUserRecord user : page.getValues()) {
                LOGGER.info(E.PIG.concat(E.PIG) + "Auth User display name: " + user.getDisplayName());
                mList.add(user);
            }
            page = page.getNextPage();
        }

        return mList;
    }

    public void deleteAuthUsers() throws Exception {
        LOGGER.info(E.WARNING.concat(E.WARNING.concat(E.WARNING)
                .concat(" DELETING ALL AUTH USERS from Firebase .... ").concat(E.RED_DOT)));
        List<ExportedUserRecord> list = getAuthUsers();
        for (ExportedUserRecord exportedUserRecord : list) {
            if (exportedUserRecord.getEmail() != null) {
                if (!exportedUserRecord.getEmail().equalsIgnoreCase("aubrey@gmail.com")) {
                    FirebaseAuth.getInstance().deleteUser(exportedUserRecord.getUid());
                        LOGGER.info(E.RED_DOT.concat(E.RED_DOT) + " Successfully deleted user ");
                }
            } else {
                FirebaseAuth.getInstance().deleteUser(exportedUserRecord.getUid());
                LOGGER.info(E.RED_DOT.concat(E.RED_DOT) + " Successfully deleted user ");
            }
        }
    }

    /**
     * Delete a collection in batches to avoid out-of-memory errors.
     * Batch size may be tuned based on document size (atmost 1MB) and application requirements.
     */
    private void deleteCollection(CollectionReference collection, int batchSize) {
        try {
            // retrieve a small batch of documents to avoid out-of-memory errors
            ApiFuture<QuerySnapshot> future = collection.limit(batchSize).get();
            int deleted = 0;
            // future.get() blocks on document retrieval
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete();
                ++deleted;
                LOGGER.info(E.RECYCLE.concat(document.getReference().getPath()
                        .concat(" deleted")));
            }
            if (deleted >= batchSize) {
                // retrieve and delete another batch
                deleteCollection(collection, batchSize);
            }
        } catch (Exception e) {
            LOGGER.info(E.NOT_OK.concat(E.ERROR) + "Error deleting collection : " + e.getMessage());
        }
    }

    private final List<ProjectLocation> projectLocations = new ArrayList<>();

    private void setLocations() {
        ProjectLocation loc1 = new ProjectLocation(latitudeLanseria, longitudeLanseria, "Lanseria Road Upgrades");
        projectLocations.add(loc1);
        ProjectLocation loc2 = new ProjectLocation(latitudeHarties, longitudeHarties, "Hartebeestpoort Shopping Centre");
        projectLocations.add(loc2);
        ProjectLocation loc3 = new ProjectLocation(latitudeJHB, longitudeJHB, "Johannesburg Road ProjectX");
        projectLocations.add(loc3);
        ProjectLocation loc4 = new ProjectLocation(latitudeRandburg, longitudeRandburg, "Randburg Road Upgrades");
        projectLocations.add(loc4);
        ProjectLocation loc5 = new ProjectLocation(latitudeRosebank, longitudeRosebank, "Rosebank Education Complex");
        projectLocations.add(loc5);
        ProjectLocation loc6 = new ProjectLocation(latitudeSandton, longitudeSandton, "Sandton Sanitation Project");
        projectLocations.add(loc6);
        ProjectLocation loc7 = new ProjectLocation(-25.731340, 28.218370, "Pretoria Road Upgrades");
        projectLocations.add(loc7);
        ProjectLocation loc8 = new ProjectLocation(-26.033, 27.983, "Fourways University Construction");
        projectLocations.add(loc8);
        ProjectLocation loc9 = new ProjectLocation(-25.98953, 28.12843, "Midrand Engineering Project");
        projectLocations.add(loc9);
        ProjectLocation loc10 = new ProjectLocation(-26.1844, 27.70203, "Randfontein Road Upgrades");
        projectLocations.add(loc10);
        ProjectLocation loc11 = new ProjectLocation(-26.08577, 27.77515, "Krugersdorp Water & Sanitation Project");
        projectLocations.add(loc11);
        ProjectLocation loc12 = new ProjectLocation(-25.9964, 28.2268, "Tembisa Education Complex");
        projectLocations.add(loc12);
        ProjectLocation loc13 = new ProjectLocation(-25.773, 28.068, "Atteridgeville Road Engineering Project");
        projectLocations.add(loc13);
        ProjectLocation loc14 = new ProjectLocation(-25.77560, 27.85987, "Oberon Water & Sanitation Project");
        projectLocations.add(loc14);
        ProjectLocation loc15 = new ProjectLocation(-25.7605543, 27.8525863, "Kingfisher Drive Road Upgrade");
        projectLocations.add(loc15);
        ProjectLocation loc16 = new ProjectLocation(-25.934042, 27.929928, "Lanseria Electrical SubStation");
        projectLocations.add(loc16);
        ProjectLocation loc17 = new ProjectLocation(-26.26611, 27.865833, "Soweto Water & Sanitation");
        projectLocations.add(loc17);
        ProjectLocation loc18 = new ProjectLocation(-25.93312, 28.01213, "Diepsloot Education Complex");
        projectLocations.add(loc18);
        ProjectLocation loc19 = new ProjectLocation(-26.483333, 27.866667, "Orange Farm Water Works");
        projectLocations.add(loc19);
        ProjectLocation loc20 = new ProjectLocation(-25.762438, 27.854500, "Pecanwood Crescent Dam Works");
        projectLocations.add(loc20);
        ProjectLocation loc21 = new ProjectLocation(-25.756321, 27.854125, "Pecanwood Boat Club Renovations");
        projectLocations.add(loc21);

        LOGGER.info(E.RAINBOW + E.RAINBOW +
                "Test Project Locations have been set: " + projectLocations.size());
    }

    private static class ProjectLocation {
        double latitude, longitude;
        String name;

        public ProjectLocation(double latitude, double longitude, String name) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.name = name;
        }
    }

}
