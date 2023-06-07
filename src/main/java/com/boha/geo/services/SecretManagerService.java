package com.boha.geo.services;

import com.boha.geo.util.E;
import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import com.google.cloud.spring.secretmanager.SecretManagerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;
import java.util.zip.CRC32C;
import java.util.zip.Checksum;

/**
 * Works with the GCP Secret Manager to retrieve keys
 */
@RequiredArgsConstructor
@Service
public class SecretManagerService {
    static final Logger LOGGER = Logger.getLogger(SecretManagerService.class.getSimpleName());

    @Value("${projectId}")
    private String projectId;
    @Value("${placesAPIKeyName}")
    private String placesAPIKeyName;
    @Value("${secretsVersion}")
    private String secretsVersion;

    @Value("${mongoSecretsName}")
    private String mongoSecretsName;

    @Value("${mongoSecretsVersion}")
    private String mongoSecretsVersion;

    @Value("${realmSecretsName}")
    private String realmSecretsName;

    @Value("${realmSecretsVersion}")
    private String realmSecretsVersion;

    private final Environment environment;
    private final SecretManagerTemplate secretManagerTemplate;


    // Access the payload for the given secret version if one exists. The version
    // can be a version number as a string (e.g. "5") or an alias (e.g. "latest").
    public String getPlacesAPIKey()
            throws Exception {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        LOGGER.info(E.RED_APPLE+E.RED_APPLE+E.RED_APPLE+E.RED_APPLE
                +" SecretMgr: getPlacesAPIKey: projectId: " + projectId);
        String payload = null;
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(projectId, placesAPIKeyName, secretsVersion);
            // Access the secret version.
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);

            // Verify checksum. The used library is available in Java 9+.
            // If using Java 8, you may use the following:
            // https://github.com/google/guava/blob/e62d6a0456420d295089a9c319b7593a3eae4a83/guava/src/com/google/common/hash/Hashing.java#L395
            byte[] data = response.getPayload().getData().toByteArray();
            Checksum checksum = new CRC32C();
            checksum.update(data, 0, data.length);
            if (response.getPayload().getDataCrc32C() != checksum.getValue()) {
                LOGGER.info("Data corruption detected.");
                throw new Exception("Data corruption detected");
            }

            // Print the secret payload.
            //
            // WARNING: Do not print the secret in a production environment - this
            // snippet is showing how to access the secret material.
             payload = response.getPayload().getData().toStringUtf8();
            String check = payload.substring(0,4);
            if (check.contains("AIza")) {
                LOGGER.info(E.RED_DOT + E.RED_DOT + E.RED_DOT +
                        " Secret Payload contains: " + check);
            }

        } catch (Exception e) {
            LOGGER.severe("Problem with secrtes: " + e.getMessage());
            e.printStackTrace();
        }
        return payload;
    }
    public String getMongoString()
            throws Exception {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        LOGGER.info(E.RED_APPLE+E.RED_APPLE+E.RED_APPLE+E.RED_APPLE
                +" SecretMgr: getMongoString: projectId: " + projectId);
//        String m =  this.secretManagerTemplate.getSecretString(
//                "sm://"+mongoSecretsName+"/"+mongoSecretsVersion);

        String m =  this.secretManagerTemplate.getSecretString(
                "sm://"+realmSecretsName+"/"+realmSecretsVersion);
        String mongoString = m.trim();

        LOGGER.info(E.RED_APPLE+E.RED_APPLE+E.RED_APPLE+E.RED_APPLE + mongoString);
        return mongoString;
    }

}
