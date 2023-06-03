package com.boha.geo.controllers;

import com.boha.geo.GeoApplication;
import com.boha.geo.models.CityPlace;
import com.boha.geo.models.KillResponse;
import com.boha.geo.monitor.data.City;
import com.boha.geo.monitor.data.Organization;
import com.boha.geo.monitor.services.MessageService;
import com.boha.geo.repos.OrganizationRepository;
import com.boha.geo.services.*;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;
@RequiredArgsConstructor
@RestController
public class TheDevController {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = Logger.getLogger(GeoApplication.class.getSimpleName());
    private static final String alien = E.AMP+E.AMP+E.AMP;
    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;

    final PlacesService placesService;
    final MongoService mongoService;
    final UserService userService;
    final CityService cityService;
    final StorageService storageService;

    final MessageService messageService;

    final OrganizationRepository organizationRepository;

    @GetMapping("/addUsers")
    private ResponseEntity<Object> addUsers() {
        try {
            int users = userService.addUsers();
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, " + users + " users");
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/killUser")
    private ResponseEntity<Object> killUser(@RequestParam String userId, @RequestParam String killerId) {
        try {
            KillResponse killMessage = messageService.sendKillMessage(userId,killerId);
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, killMessage sent to " + killMessage.getUser().getName() + ", he be dead soon! ");
            return ResponseEntity.ok(killMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/getPlacesNear")
    private ResponseEntity<Object> getPlacesNear(@RequestParam double latitude,
                                                 @RequestParam double longitude,
                                                 @RequestParam double minDistanceInMetres,
                                                 @RequestParam double maxDistanceInMetres) {
        try {
            List<CityPlace> places = placesService.getPlacesNear(latitude,longitude,
                    minDistanceInMetres,maxDistanceInMetres);
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, " + places.size() + " places");
            return ResponseEntity.ok(places);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/getCitiesNear")
    private ResponseEntity<Object> getCitiesNear(@RequestParam double latitude,
                                                 @RequestParam double longitude,
                                                 @RequestParam double minDistanceInMetres,
                                                 @RequestParam double maxDistanceInMetres) {
        try {
            List<City> citiesNear = cityService.getCitiesNear(latitude,longitude,
                    minDistanceInMetres,maxDistanceInMetres);
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, " + citiesNear.size() + " cities");
            return ResponseEntity.ok(citiesNear);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/getCitiesInProvince")
    private ResponseEntity<Object> getCitiesInProvince(@RequestParam String province) {
        try {
            List<City> citiesInProvince
                    = cityService.getCitiesInProvince(province);
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, " + citiesInProvince.size() + " Cities In Province");
            return ResponseEntity.ok(citiesInProvince);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/getPlacesByCityName")
    private ResponseEntity<Object> getPlacesByCityName(@RequestParam String city) {
        try {
            List<CityPlace> placesByCityName
                    = placesService.getPlacesByCityName(city);
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, " + placesByCityName.size() + " places in " + city);
            return ResponseEntity.ok(placesByCityName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/getPlacesByCityNameAndType")
    private ResponseEntity<Object> getPlacesByCityNameAndType(
            @RequestParam String city, @RequestParam String type) {
        try {
            List<CityPlace> placesByCityName
                    = placesService.getPlacesByCityNameAndType(city,type);
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, " + placesByCityName.size() + " places in "
                    + city + " - type: " + type);
            return ResponseEntity.ok(placesByCityName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/countPlaces")
    private ResponseEntity<Object> countPlaces() {
        try {
            long placesByCount
                    = placesService.countPlaces();
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning, places counted: " + placesByCount);
            return ResponseEntity.ok(placesByCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/addCities")
    private ResponseEntity<Object> addCities() {
        try {
            long cities = mongoService.addCitiesToDB();
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " addCities Returning " + cities + " cities");
            return ResponseEntity.ok("added " + cities + " cities");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }
    @GetMapping("/addCityPlaces")
    private ResponseEntity<Object> addCityPlaces() {
        try {
            String result = placesService.loadCityPlaces();
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning " + result + " " + E.RED_APPLE);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @GetMapping("/loadSpecialCityPlaces")
    private ResponseEntity<Object> loadSpecialCityPlaces() {
        try {
            String result = placesService.loadSpecialCityPlaces();
            logger.info(E.BLUE_HEART + E.BLUE_HEART + E.CHECK +
                    " MainController Returning " + result + " " + E.RED_APPLE);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @GetMapping("/testUploadPhoto")
    private ResponseEntity<Object> testUploadPhoto() {
        try {
            String url = storageService.testUploadPhoto();
            if (url == null) {
                logger.severe("The result is fucking NULL? WTF??");
                throw new RuntimeException("Result of call is null");
            }
            TestBag testBag = new TestBag(url,  DateTime.now().toDateTimeISO().toString());
            logger.info(E.BLUE_HEART + E.BLUE_HEART +
                    " MainController Returning testUploadPhoto result: " + url + " from cloud storage");
            return ResponseEntity.ok(testBag);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/findOrganizationById")
    private ResponseEntity<Object> findOrganizationById(@RequestParam String organizationId) {
        try {
            Organization organization = organizationRepository.findByOrganizationId(organizationId);
            logger.info(E.BLUE_HEART + E.BLUE_HEART +
                    " MainController Returning organization result: ");
            return ResponseEntity.ok(organization);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @Data
    public static class TestBag {
        private String url, date;

        public TestBag(String url, String date) {
            this.url = url;
            this.date = date;
        }
    }

}
