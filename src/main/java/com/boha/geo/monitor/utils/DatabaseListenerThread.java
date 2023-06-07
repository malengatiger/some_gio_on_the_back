package com.boha.geo.monitor.utils;

import com.boha.geo.monitor.data.*;
import com.boha.geo.monitor.services.MessageService;
import com.boha.geo.repos.AppErrorRepository;
import com.boha.geo.util.E;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.FullDocument;
import org.joda.time.DateTime;

import java.util.logging.Logger;

public class DatabaseListenerThread extends Thread {
    static final Logger LOGGER = Logger.getLogger(DatabaseListener.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    final MongoClient mongoClient;
    final MessageService messageService;
    final AppErrorRepository appErrorRepository;
    private final static String mm = "🔶🔶🔶🔶🔶 DatabaseListenerThread \uD83C\uDF4E\uD83C\uDF4E ";

    public DatabaseListenerThread(MongoClient mongoClient,
                                  MessageService messageService,
                                  AppErrorRepository appErrorRepository) {
        this.mongoClient = mongoClient;
        this.appErrorRepository = appErrorRepository;
        this.messageService = messageService;
    }

    public void run() {
        LOGGER.info(mm
                + " DatabaseListenerThread:  \uD83C\uDF4E initialize Realm listeners in thread");
        MongoDatabase db = mongoClient.getDatabase("geo");

        MongoCollection<Photo> photos = db.getCollection("Photo", Photo.class);
        MongoCollection<Video> videos = db.getCollection("Video", Video.class);
        MongoCollection<Audio> audios = db.getCollection("Audio", Audio.class);
        MongoCollection<GeofenceEvent> geoEvents = db.getCollection("GeofenceEvent",
                GeofenceEvent.class);
        MongoCollection<SettingsModel> settings = db.getCollection("SettingsModel",
                SettingsModel.class);


        ChangeStreamIterable<Photo> photoChangeStream = photos.watch()
                .fullDocument(FullDocument.DEFAULT);
        ChangeStreamIterable<Video> videoChangeStream = videos.watch()
                .fullDocument(FullDocument.DEFAULT);
        ChangeStreamIterable<Audio> audioChangeStream = audios.watch()
                .fullDocument(FullDocument.DEFAULT);
        ChangeStreamIterable<GeofenceEvent> geoChangeStream = geoEvents.watch()
                .fullDocument(FullDocument.DEFAULT);
        ChangeStreamIterable<SettingsModel> settingsChangeStream = settings.watch()
                .fullDocument(FullDocument.DEFAULT);

        LOGGER.info(mm
                + " DatabaseListenerThread:  \uD83C\uDF4E listening to photo, video, audio, settings & geofenceEvent");

        settingsChangeStream.forEach(settingsModel -> {

            try {
                assert settingsModel.getFullDocument() != null;

                LOGGER.info(mm
                        + " settingsChangeStream: SettingsModel added, send FCM message? : "
                        + G.toJson(settingsModel.getFullDocument().getOrganizationId()));
                SettingsModel p = settingsModel.getFullDocument();
                messageService.sendMessage(p);
            } catch (FirebaseMessagingException e) {
                AppError error = new AppError();
                error.setErrorMessage("FCM send settingsModel message error: " + e.getMessage());
                error.setManufacturer("Linux");
                error.setBrand("Gio Backend");
                error.setCreated(DateTime.now().toDateTimeISO().toString());
                appErrorRepository.insert(error);
            }

        });
        photoChangeStream.forEach(photo -> {
            LOGGER.info(mm
                    + " photoChangeStream: Photo added, send FCM message? : "
                    + G.toJson(photo));
            try {
                assert photo.getFullDocument() != null;
                LOGGER.info(mm
                        + " photoChangeStream: Photo added, send FCM message? : "
                        + G.toJson(photo.getFullDocument().getOrganizationId()));
                Photo p = photo.getFullDocument();
                messageService.sendMessage(p);
            } catch (FirebaseMessagingException e) {
                AppError error = new AppError();
                error.setErrorMessage("FCM send photo message error: " + e.getMessage());
                error.setManufacturer("Linux");
                error.setBrand("Gio Backend");
                error.setCreated(DateTime.now().toDateTimeISO().toString());
                appErrorRepository.insert(error);
            }

        });
        videoChangeStream.forEach(video -> {
            LOGGER.info(mm
                    + " videoChangeStream: Video added, send FCM message? : "
                    + G.toJson(video));
            try {
                assert video.getFullDocument() != null;
                LOGGER.info(mm
                        + " videoChangeStream: Video added, send FCM message? : "
                        + G.toJson(video.getFullDocument().getOrganizationId()));
                messageService.sendMessage(video.getFullDocument());
            } catch (FirebaseMessagingException e) {
                AppError error = new AppError();
                error.setErrorMessage("FCM send video message error: " + e.getMessage());
                error.setManufacturer("Linux");
                error.setBrand("Gio Backend");
                error.setCreated(DateTime.now().toDateTimeISO().toString());
                appErrorRepository.insert(error);
            }

        });
        audioChangeStream.forEach(audio -> {

            try {
                assert audio.getFullDocument() != null;
                LOGGER.info(mm
                        + " audioChangeStream: Audio added, send FCM message? : "
                        + G.toJson(audio.getFullDocument().getOrganizationId()));
                messageService.sendMessage(audio.getFullDocument());
            } catch (FirebaseMessagingException e) {
                AppError error = new AppError();
                error.setErrorMessage("FCM send audio error: " + e.getMessage());
                error.setManufacturer("Linux");
                error.setBrand("Gio Backend");
                error.setCreated(DateTime.now().toDateTimeISO().toString());
                appErrorRepository.insert(error);
            }

        });
        geoChangeStream.forEach(geofenceEvent -> {
            LOGGER.info(mm
                    + " audioChangeStream: GeofenceEvent added, send FCM message? : "
                    + G.toJson(geofenceEvent));
            try {
                assert geofenceEvent.getFullDocument() != null;
                LOGGER.info(mm
                        + " audioChangeStream: GeofenceEvent added, send FCM message? : "
                        + G.toJson(geofenceEvent.getFullDocument().getOrganizationId()));
                messageService.sendMessage(geofenceEvent.getFullDocument());
            } catch (FirebaseMessagingException e) {
                AppError error = new AppError();
                error.setErrorMessage("FCM send geofenceEvent error: " + e.getMessage());
                error.setManufacturer("Linux");
                error.setBrand("Gio Backend");
                error.setCreated(DateTime.now().toDateTimeISO().toString());
                appErrorRepository.insert(error);
            }

        });

        LOGGER.info(mm
                + " DatabaseListenerThread:  \uD83C\uDF4E waiting for things to happen ....");
    }
}
