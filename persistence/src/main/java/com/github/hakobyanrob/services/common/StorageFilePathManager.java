package com.github.hakobyanrob.services.common;

import com.github.hakobyanrob.services.storageDefinition.SingleFileStorageDefinitionManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageFilePathManager {
    private static final Logger logger = Logger.getLogger(SingleFileStorageDefinitionManager.class.getName());

    private final String storagePropertiesPath;
    private static final String defaultStoragePath = "storage.txt";
    private static final String storagePathKey = "storagePath";

    private String storagePath;

    public StorageFilePathManager(String storagePropertiesPath) {
        this.storagePropertiesPath = storagePropertiesPath;
    }

    /**
     * Reading the storage properties file to find where the storage is located.
     * If any errors occur default storage path is assigned.
     */
    public void readStorageProperties() {
        Properties prop = new Properties();
        String path = null;
        try (FileInputStream inputStream = new FileInputStream(storagePropertiesPath)) {
            prop.load(inputStream);
            path = prop.getProperty(storagePathKey);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to open storage properties file " + storagePropertiesPath);
        }
        if (path == null) {
            logger.log(Level.WARNING, "Failed to find storage path");
            logger.log(Level.INFO, "Using default storage path: " + defaultStoragePath);
            storagePath = defaultStoragePath;
        } else {
            storagePath = path;
        }
    }

    public String getStoragePath() {
        return storagePath;
    }
}
