package com.github.hakobyanrob.services.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StoragePropertiesManager {
    private static final Logger logger = Logger.getLogger(StoragePropertiesManager.class.getName());

    private final String storagePropertiesPath;

    private static final String storagePathKey = "storagePath";
    private String storagePath = "storage.txt";


    public StoragePropertiesManager(String storagePropertiesPath) {
        this.storagePropertiesPath = storagePropertiesPath;
        initializeProperties();
    }

    private void initializeProperties() {
        Properties prop = new Properties();
        try (FileInputStream inputStream = new FileInputStream(storagePropertiesPath)) {
            prop.load(inputStream);
            storagePath = prop.getProperty(storagePathKey);
        } catch (IOException | NumberFormatException e) {
            logger.log(Level.WARNING, "Failed to read storage properties from file "
                    + storagePropertiesPath + ". Default values will be set", e);
        }
    }

    public String getStoragePath() {
        return storagePath;
    }
}
