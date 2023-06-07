package com.boha.geo.monitor.utils;

import com.boha.geo.monitor.services.MessageService;
import com.boha.geo.repos.AppErrorRepository;
import com.boha.geo.util.E;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.logging.Logger;


@Configuration
public class DatabaseListener {
    static final Logger LOGGER = Logger.getLogger(DatabaseListener.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();
    @Autowired
    MongoClient mongoClient;
    @Autowired
    MessageService messageService;

    @Autowired
    AppErrorRepository appErrorRepository;

    @PostConstruct
    public void init() {
        LOGGER.info(E.ALIEN + E.ALIEN + E.ALIEN + E.ALIEN
                + " DatabaseListener: PostConstruct - set up listeners ");
        DatabaseListenerThread thread = new DatabaseListenerThread(
                mongoClient, messageService, appErrorRepository);
        thread.start();
        LOGGER.info(E.ALIEN + E.ALIEN + E.ALIEN + E.ALIEN
                + " DatabaseListener started in DatabaseListenerThread ... ");

    }
}