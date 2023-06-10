package com.boha.geo.monitor.services;

import com.boha.geo.models.GioSubscription;
import com.boha.geo.monitor.data.*;
import com.boha.geo.repos.OrganizationRepository;
import com.boha.geo.repos.ProjectPositionRepository;
import com.boha.geo.repos.ProjectRepository;
import com.boha.geo.repos.UserRepository;
import com.boha.geo.services.GioSubscriptionService;
import com.boha.geo.util.Constants;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class RegistrationService {
    private static final Logger LOGGER = Logger.getLogger(RegistrationService.class.getSimpleName());
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    final OrganizationRepository organizationRepository;
    final UserRepository userRepository;
    final ProjectRepository projectRepository;
    final ProjectPositionRepository projectPositionRepository;
    final DataService dataService;
    final ListService listService;
    final GioSubscriptionService gioSubscriptionService;

    public OrganizationRegistrationBag registerOrganization(OrganizationRegistrationBag orgBag) throws Exception {

        LOGGER.info(E.LEAF + E.LEAF + " Starting Organization Registration: " + orgBag.getOrganization().getName());

        OrganizationRegistrationBag registrationBag = new OrganizationRegistrationBag();
        registrationBag.setSettings(orgBag.getSettings());
        registrationBag.setOrganization(orgBag.getOrganization());
        registrationBag.setDate(DateTime.now().toDateTimeISO().toString());


        try {
            Organization org = organizationRepository.insert(orgBag.getOrganization());
            if (org != null) {
                dataService.addSettings(orgBag.getSettings());
                String password = orgBag.getUser().getPassword();
                orgBag.getUser().setPassword(null);
                User u = dataService.addUser(orgBag.getUser());
                u.setPassword(password);
                registrationBag.setOrganization(org);
                registrationBag.setUser(u);

                MyProjectBag bag = addSampleProject(org, orgBag.getLatitude(), orgBag.getLongitude());
                if (bag != null) {
                    registrationBag.setProject(bag.project);
                    registrationBag.setProjectPosition(bag.projectPosition);
                }

                LOGGER.info(E.LEAF + E.LEAF + " Organization Registered: " + org.getName());
                GioSubscription gs;
                if (orgBag.getGioSubscription() == null) {
                    gs = new GioSubscription();
                    gs.setSubscriptionId(UUID.randomUUID().toString());
                    gs.setSubscriptionType(Constants.FREE_SUBSCRIPTION);
                    gs.setDate(DateTime.now().toDateTimeISO().toString());
                    gs.setActive(0);
                    gs.setOrganizationId(org.getOrganizationId());
                    gs.setOrganizationName(org.getName());
                    gioSubscriptionService.addSubscription(gs);
                } else {
                    gs = gioSubscriptionService.addSubscription(orgBag.getGioSubscription());
                }
                registrationBag.setGioSubscription(gs);
                return registrationBag;
            }


        } catch (Exception e) {
            LOGGER.severe(E.RED_DOT + E.RED_DOT + " We have some kinda problem ...");
            e.printStackTrace();
            throw e;
        }
        return registrationBag;
    }

    private MyProjectBag addSampleProject(Organization organization, double latitude, double longitude) throws Exception {

        try {
            Project p0 = new Project();
            p0.setProjectId(UUID.randomUUID().toString());
            p0.setName("Sample Project");
            p0.setCreated(DateTime.now().toDateTimeISO().toString());
            p0.setDescription("Sample Project for learning and practice");
            p0.setOrganizationName(organization.getName());
            p0.setOrganizationId(organization.getOrganizationId());
            p0.setMonitorMaxDistanceInMetres(200);
            List<City> list = listService.findCitiesByLocation(latitude, longitude, 5);
            projectRepository.insert(p0);
            LOGGER.info(E.LEAF + E.LEAF + " Sample Organization Project added: " + p0.getName());

            ProjectPosition pPos = new ProjectPosition();
            Position position = new Position();
            position.setType("Point");
            List<Double> mList = new ArrayList<>();
            mList.add(longitude);
            mList.add(latitude);
            position.setCoordinates(mList);

            pPos.setProjectId(p0.getProjectId());
            pPos.setProjectName(p0.getName());
            pPos.setCaption("Project Position Caption");
            pPos.setOrganizationId(organization.getOrganizationId());
            pPos.setProjectPositionId(UUID.randomUUID().toString());
            pPos.setPosition(position);
//            pPos.setNearestCities(list);

            projectPositionRepository.insert(pPos);
            LOGGER.info(E.LEAF + E.LEAF + " Sample Organization Project Position added: " + p0.getName());


            LOGGER.info(E.LEAF +
                    "Project and ProjectPosition added, project: \uD83C\uDF4E " + p0.getName() + "\t \uD83C\uDF4E " + p0.getOrganizationName());
            MyProjectBag bag = new MyProjectBag(p0, pPos);
            return bag;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    record MyProjectBag(Project project, ProjectPosition projectPosition) {
    }
}
